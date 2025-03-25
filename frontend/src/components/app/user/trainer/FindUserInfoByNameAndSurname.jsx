import React, { useState } from 'react';
import { useDispatch, useSelector } from "react-redux";
import { selectAccessToken } from "../../../../redux/selectors";
import { setError, setErrorMessage } from "../../../../redux/actions";
import { MDBBtn, MDBInput } from 'mdb-react-ui-kit';
import {backgroundStyle, USER_INFO_URL} from "../../../../tools/consts";

const FindUserInfoByNameAndSurname = () => {
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [userInfoList, setUserInfoList] = useState([]);
    const accessToken = useSelector(selectAccessToken);
    const dispatch = useDispatch();

    const handleSearch = async () => {
        try {
            const response = await fetch(`${USER_INFO_URL}/search?name=${name}&surname=${surname}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                }
            });

            const data = await response.json();

            if (response.ok) {
                if (data.length > 0) {
                    setUserInfoList(data);
                } else {
                    setUserInfoList([]);
                    dispatch(setError(true));
                    dispatch(setErrorMessage('Пользователь не найден'));
                }
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage('Не удалось найти пользователя. Повторите попытку позже'));
                setUserInfoList([]);
            }
        } catch (error) {
            setUserInfoList([]);
            dispatch(setError(true));
            dispatch(setErrorMessage('Не удалось найти пользователя. Повторите попытку позже'));
        }
    };

    return (
        <div className="container-fluid p-0" style={backgroundStyle}>
            <div className="container mt-4">
                <div className="row">
                    <div className="col-md-8 offset-md-2">
                        <div className="card">
                            <div className="card-body text-center">
                                <h3 className="card-title text-center">Найти пользователя</h3>
                                <div className="form-group mb-3">
                                    <MDBInput
                                        label="Имя"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                    />
                                </div>
                                <div className="form-group mb-3">
                                    <MDBInput
                                        label="Фамилия"
                                        value={surname}
                                        onChange={(e) => setSurname(e.target.value)}
                                    />
                                </div>
                                <MDBBtn color="primary" onClick={handleSearch}>Найти</MDBBtn>
                            </div>
                        </div>
                    </div>
                </div>
                {userInfoList.length > 0 && (
                    <div className="row mt-4">
                        <div className="col-md-8 offset-md-2">
                            <div className="card">
                                <div className="card-body">
                                    <h3 className="card-title text-center">Результаты поиска</h3>
                                    <table className="table table-striped">
                                        <thead>
                                        <tr>
                                            <th>Email</th>
                                            <th>Имя</th>
                                            <th>Фамилия</th>
                                            <th>Роль</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {userInfoList.map((userInfo, index) => (
                                            <tr key={index}>
                                                <td>{userInfo.email}</td>
                                                <td>{userInfo.name}</td>
                                                <td>{userInfo.surname}</td>
                                                <td>{userInfo.role}</td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default FindUserInfoByNameAndSurname;