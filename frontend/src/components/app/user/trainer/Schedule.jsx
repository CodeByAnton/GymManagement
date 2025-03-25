import React, { useEffect, useState } from 'react';
import { MDBBtn, MDBCard, MDBCardBody, MDBCardTitle, MDBCardText } from 'mdb-react-ui-kit';
import { useDispatch, useSelector } from "react-redux";
import { selectAccessToken } from "../../../../redux/selectors";
import moment from 'moment';
import { setError, setErrorMessage, setSuccess, setSuccessMessage } from "../../../../redux/actions";

const Schedule = () => {
    const [schedule, setSchedule] = useState({});
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const accessToken = useSelector(selectAccessToken);
    const dispatch = useDispatch();

    useEffect(() => {
        fetchSchedule();
    }, [currentPage]);

    const fetchSchedule = async () => {
        try {
            const [groupTrainingsResponse, personalServicesResponse] = await Promise.all([
                fetch(`/api/v1/group-training?page=${currentPage}&size=10&eventStatus=SCHEDULED`, { headers: { 'Authorization': 'Bearer ' + accessToken } }),
                fetch(`/api/v1/personal-services?page=${currentPage}&size=10`, { headers: { 'Authorization': 'Bearer ' + accessToken } })
            ]);

            const groupTrainingsData = await groupTrainingsResponse.json();
            const personalServicesData = await personalServicesResponse.json();

            const combinedData = [
                ...groupTrainingsData.groupTrainings.map(training => ({
                    type: 'Group Training',
                    startDateTime: training.startDateTime,
                    endDateTime: training.endDateTime,
                    title: training.title,
                    description: training.description,
                    programDetails: training.programDetails,
                    maxParticipants: training.maxParticipants,
                    status: training.eventStatus,
                    passCode: training.passCode,
                    id: training.id
                })),
                ...personalServicesData.personalServices.map(service => ({
                    type: 'Personal Service',
                    startDateTime: service.startDateTime,
                    endDateTime: service.endDateTime,
                    title: service.serviceType,
                    description: `Client: ${service.clientEmail}`,
                    status: service.status,
                    passCode: service.passCode,
                    id: service.id
                }))
            ];

            const groupedByDate = combinedData.reduce((acc, item) => {
                const date = moment(item.startDateTime).format('YYYY-MM-DD');
                if (!acc[date]) {
                    acc[date] = [];
                }
                acc[date].push(item);
                return acc;
            }, {});

            setSchedule(prev => ({ ...prev, ...groupedByDate }));
            setTotalPages(groupTrainingsData.totalPages);
        } catch (error) {
            console.error('Error fetching schedule:', error);
        }
    };

    const handlePageChange = (newPage) => {
        setCurrentPage(newPage);
    };

    const handleCancelTraining = async (trainingId, trainingType) => {
        try {
            const url = trainingType === 'Group Training'
                ? '/api/v1/group-training/status'
                : '/api/v1/personal-services/status';

            const dto = trainingType === 'Group Training'
                ? { groupTrainingId: trainingId, newEventStatus: 'CANCELLED' }
                : { personalServiceId: trainingId, newEventStatus: 'CANCELLED' };

            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                },
                body: JSON.stringify(dto)
            });

            if (response.ok) {
                // Удаляем тренировку или услугу из расписания, учитывая тип
                setSchedule(prev => {
                    const newSchedule = { ...prev };
                    Object.keys(newSchedule).forEach(date => {
                        newSchedule[date] = newSchedule[date].filter(item => !(item.id === trainingId && item.type === trainingType));
                    });
                    return newSchedule;
                });
                dispatch(setSuccess(true));
                dispatch(setSuccessMessage('Training/Service cancelled successfully!'));
                setTimeout(() => {
                    dispatch(setSuccess(false));
                }, 3000);
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage('Failed to cancel training/service.'));
                setTimeout(() => {
                    dispatch(setError(false));
                }, 3000);
            }
        } catch (error) {
            console.error('Error cancelling training/service:', error);
            dispatch(setError(true));
            dispatch(setErrorMessage('Failed to cancel training/service.'));
            setTimeout(() => {
                dispatch(setError(false));
            }, 3000);
        }
    };

    return (
        <div style={{
            backgroundImage: 'url(img/sea.jpg)', // Укажите URL вашей картинки
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            minHeight: '100vh',
            padding: '20px'
        }}>
            <div className="w-75 mx-auto" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', padding: '20px', borderRadius: '10px' }}>
                {Object.keys(schedule)
                    .sort((a, b) => moment(a).diff(moment(b)))
                    .map(date => (
                        <div key={date} className="text-center mb-4">
                            <h3 className="text-center">{moment(date).format('dddd, MMMM Do YYYY')}</h3>
                            <div className="d-flex justify-content-center">
                                {schedule[date]
                                    .sort((a, b) => moment(a.startDateTime).diff(moment(b.startDateTime)))
                                    .map((item, index) => (
                                        <MDBCard key={index} className="mb-3" style={{ width: '75%' }}>
                                            <MDBCardBody>
                                                <MDBCardTitle className="text-center">{item.type}</MDBCardTitle>
                                                <MDBCardText className={"text-start"}>
                                                    <strong>Time:</strong> {moment(item.startDateTime).format('HH:mm')} - {moment(item.endDateTime).format('HH:mm')}
                                                    <br />
                                                    {item.title && <><strong>Title:</strong> {item.title} <br /></>}
                                                    {item.description && <><strong>Description:</strong> {item.description}
                                                        <br /></>}
                                                    {item.programDetails && <><strong>Program
                                                        details:</strong> {item.programDetails} <br /></>}
                                                    {item.maxParticipants && <><strong>Max
                                                        Participants:</strong> {item.maxParticipants} <br /></>}
                                                    {item.serviceType && <><strong>Service type:</strong> {item.serviceType}
                                                        <br /></>}
                                                    {item.passCode && <><strong>Client's pass code:</strong> {item.passCode}
                                                        <br /></>}
                                                </MDBCardText>
                                                <div className="text-center">
                                                    <MDBBtn color="danger"
                                                            onClick={() => handleCancelTraining(item.id, item.type)}>
                                                        Cancel
                                                    </MDBBtn>
                                                </div>
                                            </MDBCardBody>
                                        </MDBCard>
                                    ))}
                            </div>
                        </div>
                    ))}

                <div className="d-flex justify-content-between">
                    <MDBBtn onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 0}>
                        Previous
                    </MDBBtn>
                    <MDBBtn onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages - 1}>
                        Next
                    </MDBBtn>
                </div>
            </div>
        </div>
    );
};

export default Schedule;