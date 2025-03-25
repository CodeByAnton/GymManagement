import React from 'react';
import styles from '../css/message.module.css';

const ErrorMessage = ({ message, onClose }) => {
    return (
        <div className={`${styles['error-message']} alert alert-danger alert-dismissible fade show`} role="alert">
            {message}
            <button type="button" className="btn-close" data-bs-dismiss="alert" aria-label="Close" onClick={onClose}></button>
        </div>
    );
};

export default ErrorMessage;