import React from 'react';
import styles from '../css/message.module.css';

const SuccessMessage = ({ message, onClose }) => {
    return (
        <div className={`${styles['success-message']} alert alert-success alert-dismissible fade show`} role="alert">
            {message}
            <button type="button" className="btn-close" data-bs-dismiss="alert" aria-label="Close" onClick={onClose}></button>
        </div>
    );
};

export default SuccessMessage;