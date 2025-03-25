import React, { useState } from 'react';
import { useDispatch, useSelector } from "react-redux";
import { MDBInput, MDBBtn } from 'mdb-react-ui-kit';
import { selectAccessToken } from "../../../../redux/selectors";
import {handleSearchSubscription} from "../../../../tools/functions";

const ShowSubscription = () => {
    const [email, setEmail] = useState('');
    const [subscription, setSubscription] = useState(null);
    const [message, setMessage] = useState('');
    const dispatch = useDispatch();
    const accessToken = useSelector(selectAccessToken);


    const containerStyle = {
        border: 'none',
        boxShadow: 'none',
        backgroundColor: 'transparent',
    };

    const cardStyle = {
        border: 'none',
        boxShadow: 'none',
        backgroundColor: 'transparent',
    };

    const cardBodyStyle = {
        padding: '0',
    };

    return (
        <div style={containerStyle}>
            <div className="container mt-4 mb-4">
                <div className="row">
                    <div className="col-md-8 offset-md-2">
                        <div className="card" style={cardStyle}>
                            <div className="card-body" style={cardBodyStyle}>
                                <h2 className="card-title text-center">Найти абонемент</h2>
                                <div className="form-group">
                                    <MDBInput
                                        type="email"
                                        label="Введите email"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="text-center mt-3">
                                    <MDBBtn onClick={()=> handleSearchSubscription(dispatch, setSubscription, setMessage, email, accessToken)}>Найти</MDBBtn>
                                </div>
                                {message && (
                                    <div className="mt-3 text-center">
                                        <p>{message}</p>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ShowSubscription;