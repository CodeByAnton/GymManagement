import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from "react-redux";
import { selectAccessToken, selectRole } from "../../../../redux/selectors";
import { setError, setErrorMessage, setSuccess, setSuccessMessage } from "../../../../redux/actions";
import { MDBBtn, MDBInput } from 'mdb-react-ui-kit';
import { NOTIFICATIONS_URL } from "../../../../tools/consts";

const Notifications = () => {
    const [notifications, setNotifications] = useState([]);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);
    const [newNotification, setNewNotification] = useState({
        receiverEmail: '',
        message: '',
    });
    const accessToken = useSelector(selectAccessToken);
    const userRole = useSelector(selectRole);
    const dispatch = useDispatch();

    useEffect(() => {
        fetchNotifications();
    }, [page, size]);

    const fetchNotifications = async () => {
        try {
            const response = await fetch(`${NOTIFICATIONS_URL}?page=${page}&size=${size}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                }
            });

            const data = await response.json();

            if (response.ok) {
                const sortedNotifications = data.notifications.sort((a, b) => new Date(b.notificationDate) - new Date(a.notificationDate));
                setNotifications(sortedNotifications);
                setTotalPages(data.totalPages);
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage('Не удалось получить уведомления. Повторите попытку позже'));
            }
        } catch (error) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Не удалось получить уведомления. Повторите попытку позже'));
        }
    };

    const handleCheckNotification = async (id) => {
        try {
            const response = await fetch(`${NOTIFICATIONS_URL}/check`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                },
                body: JSON.stringify({ id })
            });

            const data = await response.json();

            if (response.ok) {
                dispatch(setSuccess(true));
                dispatch(setSuccessMessage('Уведомление отмечено прочитанным.'));
                fetchNotifications();
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage('Не удалось отметить уведомление прочитанным. Повторите попытку позже'));
            }
        } catch (error) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Не удалось отметить уведомление прочитанным. Повторите попытку позже'));
        }
    };

    const handleCreateNotification = async () => {
        if (userRole !== 'ADMINISTRATOR') {
            dispatch(setError(true));
            dispatch(setErrorMessage('Только администратор может создавать уведомления'));
            return;
        }

        try {
            const response = await fetch(NOTIFICATIONS_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                },
                body: JSON.stringify(newNotification)
            });

            const data = await response.json();

            if (response.ok) {
                dispatch(setSuccess(true));
                dispatch(setSuccessMessage('Уведомление успешно создано'));
                setNewNotification({
                    receiverEmail: '',
                    message: '',
                });
                fetchNotifications();
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage('Не удалось создать уведомление. Повторите попытку позже'));
            }
        } catch (error) {
            dispatch(setError(true));
            dispatch(setErrorMessage('Не удалось создать уведомление. Повторите попытку позже'));
        }
    };

    const containerStyle = {
        backgroundImage: 'url(/img/sea.jpg)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        minHeight: '100vh',
        paddingTop: '20px',
    };

    return (
        <div className="container-fluid" style={containerStyle}>
            <div className="row">
                <div className="col-md-8 offset-md-2">
                    <div className="card">
                        <div className="card-body">
                            <h3 className="card-title text-center">Список уведомлений</h3>
                            {notifications.length === 0 ? (
                                <p className="text-center">У вас пока нет уведомлений</p>
                            ) : (
                                <ul className="list-group">
                                    {notifications.sort((a, b) => new Date(b.notificationDate) - new Date(a.notificationDate)).map(notification => (
                                        <li key={notification.id} className="list-group-item d-flex justify-content-between align-items-center">
                                            <div>
                                                <strong>{notification.message}</strong>
                                                <br />
                                                <small>От: {notification.senderEmail}</small>
                                                <br />
                                                <small>Дата: {new Date(notification.notificationDate).toLocaleString()}</small>
                                            </div>
                                            <div className="d-flex align-items-center justify-content-center">
                                                {(notification.notificationStatus === 'UNCHECKED') && (
                                                    <span className="badge badge-primary badge-pill me-2">Новое</span>
                                                )}
                                                {notification.notificationStatus !== 'CHECKED' && (
                                                    <MDBBtn color="primary" size="sm" onClick={() => handleCheckNotification(notification.id)}>Отметить прочитанным</MDBBtn>
                                                )}
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                            )}
                            <div className="mt-3 text-center">
                                <MDBBtn color="secondary" onClick={() => setPage(page - 1)} disabled={page === 0}>Предыдущая</MDBBtn>
                                <MDBBtn color="secondary" className="ms-3" onClick={() => setPage(page + 1)} disabled={page >= totalPages - 1}>Следующая</MDBBtn>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            {userRole === 'ADMINISTRATOR' && (
                <div className="row mt-4">
                    <div className="col-md-8 offset-md-2">
                        <div className="card">
                            <div className="card-body text-center">
                                <h3 className="card-title text-center">Создать уведомление</h3>
                                <div className="form-group mb-3">
                                    <MDBInput
                                        label="Email получателя"
                                        value={newNotification.receiverEmail}
                                        onChange={(e) => setNewNotification({ ...newNotification, receiverEmail: e.target.value })}
                                    />
                                </div>
                                <div className="form-group mb-3">
                                    <MDBInput
                                        label="Сообщение"
                                        value={newNotification.message}
                                        onChange={(e) => setNewNotification({ ...newNotification, message: e.target.value })}
                                    />
                                </div>

                                <MDBBtn color="success" onClick={handleCreateNotification}>Создать</MDBBtn>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Notifications;