import React, { useState } from 'react';
import { backgroundStyle, Providers } from "../../../tools/consts";
import Logout from "../forms/Logout";
import styles from '../../../css/Subscription.module.css'
import CreateSubscription from "../../app/user/admin/CreateSubscription";
import ShowSubscription from "../../app/user/admin/ShowSubscription";

const SubscriptionProvider = () => {
    const subscriptionMode = {
        create: 'create',
        show: 'show'
    };

    const [mode, setMode] = useState(subscriptionMode.create);

    const toggleMode = (newMode) => {
        setMode(newMode);
    };

    return (
        <span className={styles.container} style={backgroundStyle}>
        <div className="container w-75">
            <div className="row mb-4 rounded-3 d-flex justify-content-center align-items-center">
                <div className="col-12 text-center">
                    <div className="btn-group btn-toggle bg-white">
                        <button onClick={() => setMode(subscriptionMode.create)} className={`btn btn-lg btn-default ${mode === subscriptionMode.create && "btn-primary"}`}>Создать</button>
                        <button onClick={() => setMode(subscriptionMode.show)} className={`btn btn-lg btn-default ${mode === subscriptionMode.show && "btn-primary"} `}>Проверить</button>
                    </div>
                </div>
            </div>
            <div className={styles.subscriptionForm}>
                {mode === subscriptionMode.create ? <CreateSubscription/> : <ShowSubscription/>}
            </div>
        </div>
        </span>
    );
};

export default SubscriptionProvider;