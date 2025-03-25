import React, { useState } from 'react';
import styles from '../../../../css/CreateSubscription.module.css';
import { SUBSCRIPTION_URL } from "../../../../tools/consts";
import { useDispatch, useSelector } from "react-redux";
import { selectAccessToken } from "../../../../redux/selectors";
import { setError, setErrorMessage, setSuccess, setSuccessMessage } from "../../../../redux/actions";
import { MDBBtn, MDBInput } from 'mdb-react-ui-kit';

const CreateSubscription = () => {
    const [email, setEmail] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [price, setPrice] = useState('');
    const accessToken = useSelector(selectAccessToken);
    const dispatch = useDispatch();

    const validateEmail = (email) => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateEmail(email)) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Неверный формат email'));
            return;
        }

        if (parseInt(price) <= 0) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Цена абонемента должна быть больше нуля'));
            return;
        }

        if (new Date(startDate) >= new Date(endDate)) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Дата начала должна быть меньше даты конца'));
            return;
        }

        const subscriptionDTO = {
            email,
            purchaseDate: new Date().toISOString().split('T')[0],
            startDate,
            endDate,
            price: parseInt(price)
        };

        try {
            const response = await fetch(SUBSCRIPTION_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                },
                body: JSON.stringify(subscriptionDTO)
            });

            const data = await response.json();

            if (response.ok) {
                dispatch(setSuccess(true));
                dispatch(setSuccessMessage('Абонемент успешно создан!'));
                setEmail('');
                setStartDate('');
                setEndDate('');
                setPrice('');
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage('Не удалось создать абонемент. Повторите попытку позже'));
            }
        } catch (error) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Не удалось создать абонемент. Повторите попытку позже'));
        }
    };

    return (
        <div className={styles.subscriptionForm}>
            <h2>Создать абонемент</h2>
            <form onSubmit={handleSubmit}>
                <div className={styles.formGroup}>
                    <MDBInput
                        type="email"
                        id="email"
                        label="Email клиента"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className={styles.formGroup}>
                    <MDBInput
                        type="date"
                        id="startDate"
                        label="Дата начала"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        required
                    />
                </div>
                <div className={styles.formGroup}>
                    <MDBInput
                        type="date"
                        id="endDate"
                        label="Дата конца"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        required
                    />
                </div>
                <div className={styles.formGroup}>
                    <MDBInput
                        type="number"
                        id="price"
                        label="Цена (руб.)"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                    />
                </div>
                <MDBBtn type="submit">Создать абонемент</MDBBtn>
            </form>
        </div>
    );
};

export default CreateSubscription;