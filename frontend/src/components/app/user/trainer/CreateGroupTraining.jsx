import React, { useState, useEffect } from 'react';
import { GROUP_TRAINING_URL, GROUP_TRAINING_PROGRAM_DETAILS_URL } from "../../../../tools/consts";
import { useDispatch, useSelector } from "react-redux";
import { selectAccessToken } from "../../../../redux/selectors";
import { setError, setErrorMessage, setSuccess, setSuccessMessage } from "../../../../redux/actions";
import { MDBBtn, MDBInput } from 'mdb-react-ui-kit';

const CreateGroupTraining = () => {
    const [startDateTime, setStartDateTime] = useState('');
    const [duration, setDuration] = useState('');
    const [maxParticipants, setMaxParticipants] = useState('');
    const [programDetailsId, setProgramDetailsId] = useState('');
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [programDetails, setProgramDetails] = useState('');
    const [isNewProgramDetails, setIsNewProgramDetails] = useState(false);
    const accessToken = useSelector(selectAccessToken);
    const dispatch = useDispatch();

    const [existingProgramDetails, setExistingProgramDetails] = useState([]);

    // Загрузка существующих GroupTrainingProgramDetails
    const fetchExistingProgramDetails = async () => {
        try {
            const response = await fetch(GROUP_TRAINING_PROGRAM_DETAILS_URL, {
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                }
            });
            const data = await response.json();
            setExistingProgramDetails(data);
        } catch (error) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Не удалось загрузить существующие программы тренировок'));
        }
    };

    useEffect(() => {
        fetchExistingProgramDetails();
    }, [accessToken, dispatch]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (parseInt(duration) <= 0) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Продолжительность должна быть больше нуля'));
            return;
        }

        if (parseInt(maxParticipants) <= 0) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Максимальное количество участников должно быть больше нуля'));
            return;
        }

        let groupTrainingDTO;

        if (isNewProgramDetails) {
            // Создание нового GroupTrainingProgramDetails
            const newProgramDetailsDTO = {
                title,
                description,
                programDetails
            };

            try {
                const response = await fetch(GROUP_TRAINING_PROGRAM_DETAILS_URL, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + accessToken
                    },
                    body: JSON.stringify(newProgramDetailsDTO)
                });

                const data = await response.json();

                if (response.ok) {
                    groupTrainingDTO = {
                        startDateTime,
                        duration: parseInt(duration),
                        maxParticipants: parseInt(maxParticipants),
                        programDetailsId: data.id
                    };
                } else {
                    dispatch(setError(true));
                    dispatch(setErrorMessage(data.description));
                    return;
                }
            } catch (error) {
                dispatch(setError(true));
                dispatch(setErrorMessage('Не удалось создать программу тренировки'));
                return;
            }
        } else {
            groupTrainingDTO = {
                startDateTime,
                duration: parseInt(duration),
                maxParticipants: parseInt(maxParticipants),
                programDetailsId
            };
        }

        try {
            const response = await fetch(GROUP_TRAINING_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                },
                body: JSON.stringify(groupTrainingDTO)
            });

            const data = await response.json();

            if (response.ok) {
                dispatch(setSuccess(true));
                dispatch(setSuccessMessage('Групповая тренировка успешно создана!'));
                setStartDateTime('');
                setDuration('');
                setMaxParticipants('');
                setProgramDetailsId('');
                setTitle('');
                setDescription('');
                setProgramDetails('');
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage(data.description));
            }
        } catch (error) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Не удалось создать групповую тренировку. Повторите попытку позже'));
        }
    };

    return (
        <div className="w-75" style={{ margin: '0 auto', padding: '20px', border: '1px solid #ccc', borderRadius: '5px', backgroundColor: 'white' }}>
            <h2 style={{ textAlign: 'center' }}>Создать групповую тренировку</h2>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '15px' }}>
                    <label htmlFor="startDateTime" style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Время начала</label>
                    <MDBInput
                        type="datetime-local"
                        id="startDateTime"
                        placeholder="Время начала"
                        value={startDateTime}
                        onChange={(e) => setStartDateTime(e.target.value)}
                        required
                    />
                </div>
                <div style={{ marginBottom: '15px' }}>
                    <MDBInput
                        type="number"
                        id="duration"
                        label="Продолжительность (минуты)"
                        value={duration}
                        onChange={(e) => setDuration(e.target.value)}
                        required
                    />
                </div>
                <div style={{ marginBottom: '15px' }}>
                    <MDBInput
                        type="number"
                        id="maxParticipants"
                        label="Максимальное количество участников"
                        value={maxParticipants}
                        onChange={(e) => setMaxParticipants(e.target.value)}
                        required
                    />
                </div>
                <div style={{ marginBottom: '15px' }}>
                    <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
                        Выберите программу тренировки:
                    </label>
                    <div style={{ display: 'flex', alignItems: 'center' }}>
                        <input
                            type="radio"
                            id="existingProgramDetails"
                            name="programDetails"
                            checked={!isNewProgramDetails}
                            onChange={() => {
                                setIsNewProgramDetails(false);
                                fetchExistingProgramDetails();
                            }}
                        />
                        <label htmlFor="existingProgramDetails" style={{ marginLeft: '5px' }}>Существующая программа</label>
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center' }}>
                        <input
                            type="radio"
                            id="newProgramDetails"
                            name="programDetails"
                            checked={isNewProgramDetails}
                            onChange={() => setIsNewProgramDetails(true)}
                        />
                        <label htmlFor="newProgramDetails" style={{ marginLeft: '5px' }}>Новая программа</label>
                    </div>
                </div>
                {!isNewProgramDetails && (
                    <div style={{ marginBottom: '15px' }}>
                        <select
                            id="programDetailsId"
                            value={programDetailsId}
                            onChange={(e) => setProgramDetailsId(e.target.value)}
                            required
                            style={{ width: '100%', padding: '8px', border: '1px solid #ccc', borderRadius: '4px' }}
                        >
                            <option value="">Выберите программу тренировки</option>
                            {existingProgramDetails.map(details => (
                                <option key={details.id} value={details.id}>{details.title}</option>
                            ))}
                        </select>
                    </div>
                )}
                {isNewProgramDetails && (
                    <>
                        <div style={{ marginBottom: '15px' }}>
                            <MDBInput
                                type="text"
                                id="title"
                                label="Название программы"
                                value={title}
                                onChange={(e) => setTitle(e.target.value)}
                                required
                            />
                        </div>
                        <div style={{ marginBottom: '15px' }}>
                            <MDBInput
                                type="text"
                                id="description"
                                label="Описание программы"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                required
                            />
                        </div>
                        <div style={{ marginBottom: '15px' }}>
                            <MDBInput
                                type="text"
                                id="programDetails"
                                label="Детали программы"
                                value={programDetails}
                                onChange={(e) => setProgramDetails(e.target.value)}
                                required
                            />
                        </div>
                    </>
                )}
                <MDBBtn type="submit" style={{ width: '100%', padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
                    Создать тренировку
                </MDBBtn>
            </form>
        </div>
    );
};

export default CreateGroupTraining;