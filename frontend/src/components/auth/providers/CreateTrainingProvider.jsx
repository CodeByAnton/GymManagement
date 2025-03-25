import React, {useState} from 'react';
import styles from "../../../css/Subscription.module.css";
import {backgroundStyle} from "../../../tools/consts";
import CreatePersonalTraining from "../../app/user/trainer/CreatePersonalTraining";
import CreateGroupTraining from "../../app/user/trainer/CreateGroupTraining";

const CreateTrainingProvider = () => {
    const createTrainingMode = {
        group: 'group',
        personal: 'personal'
    };

    const [mode, setMode] = useState(createTrainingMode.personal);

    const toggleMode = (newMode) => {
        setMode(newMode);
    };

    return (
        <span className={styles.container} style={backgroundStyle}>
        <div className="container w-75">
            <div className="row mb-4 rounded-3 d-flex justify-content-center align-items-center">
                <div className="col-12 text-center">
                    <div className="btn-group btn-toggle bg-white">
                        <button onClick={() => setMode(createTrainingMode.personal)} className={`btn btn-lg btn-default ${mode === createTrainingMode.personal && "btn-primary"}`}>Персональная тренировка</button>
                        <button onClick={() => setMode(createTrainingMode.group)} className={`btn btn-lg btn-default ${mode === createTrainingMode.group && "btn-primary"} `}>Групповая тренировка</button>
                    </div>
                </div>
            </div>

            {mode === createTrainingMode.personal ? <CreatePersonalTraining/> : <CreateGroupTraining/>}

        </div>
        </span>
    );
};

export default CreateTrainingProvider;