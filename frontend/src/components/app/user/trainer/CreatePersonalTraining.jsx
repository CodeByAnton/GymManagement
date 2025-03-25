import React, { useState } from 'react';
import { PERSONAL_SERVICE_URL } from "../../../../tools/consts";
import { useDispatch, useSelector } from "react-redux";
import { selectAccessToken } from "../../../../redux/selectors";
import { setError, setErrorMessage, setSuccess, setSuccessMessage } from "../../../../redux/actions";
import { MDBBtn, MDBInput } from 'mdb-react-ui-kit';

const CreatePersonalTraining = () => {
    const [clientEmail, setClientEmail] = useState('');
    const [startDateTime, setStartDateTime] = useState('');
    const [serviceType, setServiceType] = useState('');
    const [duration, setDuration] = useState('');
    const accessToken = useSelector(selectAccessToken);
    const dispatch = useDispatch();

    const validateEmail = (email) => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateEmail(clientEmail)) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Неверный формат email'));
            return;
        }

        if (parseInt(duration) <= 0) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Продолжительность должна быть больше нуля'));
            return;
        }

        const personalServiceDTO = {
            clientEmail,
            startDateTime,
            serviceType,
            duration: parseInt(duration)
        };

        try {
            const response = await fetch(PERSONAL_SERVICE_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                },
                body: JSON.stringify(personalServiceDTO)
            });

            const data = await response.json();

            if (response.ok) {
                dispatch(setSuccess(true));
                dispatch(setSuccessMessage('Персональная тренировка успешно создана!'));
                setClientEmail('');
                setStartDateTime('');
                setServiceType('');
                setDuration('');
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage(data.description));
            }
        } catch (error) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Не удалось создать персональную тренировку. Повторите попытку позже'));
        }
    };

    return (
        <div className="w-75" style={{ margin: '0 auto', padding: '20px', border: '1px solid #ccc', borderRadius: '5px', backgroundColor: 'white' }}>
            <h2 style={{ textAlign: 'center' }}>Создать персональную тренировку</h2>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '15px' }}>
                    <MDBInput
                        type="email"
                        id="email"
                        label="Email клиента"
                        value={clientEmail}
                        onChange={(e) => setClientEmail(e.target.value)}
                        required
                    />
                </div>
                <div style={{ marginBottom: '15px' }}>
                    <label htmlFor="startTime" style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Время начала</label>
                    <MDBInput
                        type="datetime-local"
                        id="startTime"
                        placeholder="Время начала"
                        value={startDateTime}
                        onChange={(e) => setStartDateTime(e.target.value)}
                        required
                    />
                </div>
                <div style={{ marginBottom: '15px' }}>
                    <label htmlFor="serviceType" style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Тип услуги</label>
                    <select
                        id="serviceType"
                        value={serviceType}
                        onChange={(e) => setServiceType(e.target.value)}
                        required
                        style={{ width: '100%', padding: '8px', border: '1px solid #ccc', borderRadius: '4px' }}
                    >
                        <option value="">Выберите тип услуги</option>
                        <option value="TRAINING">TRAINING</option>
                        <option value="CONSULTATION">CONSULTATION</option>
                        <option value="MESSAGE">MESSAGE</option>
                    </select>
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
                <MDBBtn type="submit" style={{ width: '100%', padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
                    Создать тренировку
                </MDBBtn>
            </form>
        </div>
    );
};

export default CreatePersonalTraining;