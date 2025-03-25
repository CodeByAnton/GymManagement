import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from "react-redux";
import { selectAccessToken } from "../../../../redux/selectors";
import { setError, setErrorMessage } from "../../../../redux/actions";
import { USER_INFO_URL } from "../../../../tools/consts";
import {handleSearchSubscription} from "../../../../tools/functions";
import Logout from "../../../auth/forms/Logout";

const Profile = () => {
    const [userInfo, setUserInfo] = useState(null);
    const [subscription, setSubscription] = useState(null);
    const [subscriptionMessage, setSubscriptionMessage] = useState('');
    const accessToken = useSelector(selectAccessToken);
    const dispatch = useDispatch();

    useEffect(() => {
        fetchUserInfo();
    }, []);

    const fetchUserInfo = async () => {
        try {
            const response = await fetch(USER_INFO_URL, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                }
            });

            const data = await response.json();

            if (response.ok) {
                setUserInfo(data);
                if (data.role === 'CLIENT') {
                    handleSearchSubscription(dispatch, setSubscription, setSubscriptionMessage, data.email, accessToken);
                }
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage('Не удалось получить информацию о пользователе. Повторите попытку позже'));
            }
        } catch (error) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Не удалось получить информацию о пользователе. Повторите попытку позже'));
        }
    };

    const containerStyle = {
        backgroundImage: 'url(/img/sea.jpg)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        minHeight: '100vh',
        paddingTop: '20px',
        color: 'white',
    };

    return (
        <div className="container-fluid text-center" style={containerStyle}>
            <div className="row mb-2">
                <div className="col-md-8 offset-md-2">
                    <div className="card bg-white text-black">
                        <div className="card-body">
                            <h2 className="card-title text-center">Профиль пользователя</h2>
                            <hr/>
                            {userInfo ? (
                                <div>
                                    <p className="text-start"><strong>Email:</strong> {userInfo.email}</p>
                                    <p className="text-start"><strong>Имя:</strong> {userInfo.name}</p>
                                    <p className="text-start"><strong>Фамилия:</strong> {userInfo.surname}</p>
                                    <p className={`text-start ${(userInfo.role !== 'CLIENT') && 'mb-3'}`}><strong>Роль:</strong> {userInfo.role}</p>
                                    {userInfo.role === 'CLIENT' && (
                                        <p className="text-start mb-3"><strong>Абонемент:</strong> {subscriptionMessage}</p>
                                    )}
                                    <Logout/>
                                </div>
                            ) : (
                                <p>Загрузка информации...</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>

        </div>
    );
};

export default Profile;