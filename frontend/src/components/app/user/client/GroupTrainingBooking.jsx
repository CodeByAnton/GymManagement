import React, { useEffect, useState } from 'react';
import { MDBBtn, MDBCard, MDBCardBody, MDBCardTitle, MDBCardText } from 'mdb-react-ui-kit';
import { useDispatch, useSelector } from "react-redux";
import { selectAccessToken } from "../../../../redux/selectors";
import moment from 'moment';
import { setError, setErrorMessage, setSuccess, setSuccessMessage } from "../../../../redux/actions";

const GroupTrainingBooking = () => {
    const [bookedTrainings, setBookedTrainings] = useState({});
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const accessToken = useSelector(selectAccessToken);
    const dispatch = useDispatch();

    useEffect(() => {
        fetchBookedTrainings();
    }, [currentPage]);

    const fetchBookedTrainings = async () => {
        try {
            const response = await fetch(`/api/v1/group-training?page=${currentPage}&size=10&eventStatus=SCHEDULED`, {
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                }
            });

            const data = await response.json();

            const groupedByDate = data.groupTrainings.reduce((acc, item) => {
                const date = moment(item.startDateTime).format('YYYY-MM-DD');
                if (!acc[date]) {
                    acc[date] = [];
                }
                acc[date].push(item);
                return acc;
            }, {});

            setBookedTrainings(groupedByDate);
            setTotalPages(data.totalPages);
        } catch (error) {
            console.error('Error fetching booked trainings:', error);
        }
    };

    const handlePageChange = (newPage) => {
        setCurrentPage(newPage);
    };

    const handleBookTraining = async (trainingId) => {
        try {
            const response = await fetch('/api/v1/group-training/book', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                },
                body: JSON.stringify({ groupTrainingId: trainingId })
            });

            if (response.ok) {
                dispatch(setSuccess(true));
                dispatch(setSuccessMessage('Successfully booked training!'));
                setTimeout(() => {
                    dispatch(setSuccess(false));
                }, 3000);
                fetchBookedTrainings();
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage('Failed to book training.'));
                setTimeout(() => {
                    dispatch(setError(false));
                }, 3000);
            }
        } catch (error) {
            console.error('Error booking training:', error);
            dispatch(setError(true));
            dispatch(setErrorMessage('Failed to book training.'));
            setTimeout(() => {
                dispatch(setError(false));
            }, 3000);
        }
    };

    return (
        <div style={{
            backgroundImage: 'url(img/sea.jpg)',
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            minHeight: '100vh',
            padding: '20px'
        }}>
            <div className="w-75 mx-auto" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', padding: '20px', borderRadius: '10px' }}>
                {Object.keys(bookedTrainings)
                    .sort((a, b) => moment(a).diff(moment(b)))
                    .map(date => (
                        <div key={date} className="text-center mb-4">
                            <h3 className="text-center">{moment(date).format('dddd, MMMM Do YYYY')}</h3>
                            <div className="d-flex justify-content-center">
                                {bookedTrainings[date]
                                    .sort((a, b) => moment(a.startDateTime).diff(moment(b.startDateTime)))
                                    .map((item, index) => (
                                        <MDBCard key={index} className="mb-3" style={{ width: '75%' }}>
                                            <MDBCardBody>
                                                <MDBCardTitle className="text-center">{item.title}</MDBCardTitle>
                                                <MDBCardText className={"text-start"}>
                                                    <strong>Time:</strong> {moment(item.startDateTime).format('HH:mm')} - {moment(item.endDateTime).format('HH:mm')}
                                                    <br />
                                                    {item.description && <><strong>Description:</strong> {item.description}
                                                        <br /></>}
                                                    {item.programDetails && <><strong>Program
                                                        details:</strong> {item.programDetails} <br /></>}
                                                    {item.maxParticipants && <><strong>Max
                                                        Participants:</strong> {item.maxParticipants} <br /></>}
                                                </MDBCardText>
                                                <div className="text-center">
                                                    { !item.clientHasBooking && <MDBBtn color="success"
                                                                                        onClick={() => handleBookTraining(item.id)}>
                                                        Book
                                                    </MDBBtn>}

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

export default GroupTrainingBooking;