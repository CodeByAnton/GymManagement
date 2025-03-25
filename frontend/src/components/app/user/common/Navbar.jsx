import React from 'react';
import {Pages, Role} from "../../../../tools/consts";
import styles from "../../../../css/Navbar.module.css";
import {useSelector} from "react-redux";
import {selectAuthenticated, selectRole} from "../../../../redux/selectors";

const Navbar = ({ setPage, currentPage }) => {
    const isPhone = window.innerWidth < 768;

    const role = useSelector(selectRole);
    const isAuthenticated = useSelector(selectAuthenticated);

    return (
        <div className={`bg-light sticky-top p-2 shadow-3-strong d-flex justify-content-between align-items-center ${isPhone ? "py-3" : ""}`}>

            {!isPhone && <div className="flex-grow-1 mx-3"></div>}

            <div className={`${isPhone ? "d-flex" : "d-inline-flex"}`}>

                <div className=" px-lg-5 flex-fill text-center d-flex align-items-center">
                    <a
                        className={`${styles.element} text-black-50 ${!isPhone && "px-4 py-2 fs-6"} ${isPhone && "px-2 py-1"} text-uppercase ${currentPage === Pages.Login ? styles.active : ''}`}
                        href=""
                        onClick={(event) => {
                            event.preventDefault();
                            setPage(Pages.Login);
                        }}
                    >
                        <i className="fa-solid fa-circle-user me-1"></i> {isAuthenticated ? "Профиль" : "Войти"}
                    </a>
                </div>
                <div
                    className={`${role !== Role.Admin && 'd-none'} px-lg-3 flex-fill text-center d-flex align-items-center`}>
                    <a
                        className={`${styles.element} text-black-50 ${!isPhone && "px-4 py-2 fs-6"} ${isPhone && "px-2 py-1"} text-uppercase ${currentPage === Pages.Applications ? styles.active : ''}`}
                        href=""
                        onClick={(event) => {
                            event.preventDefault();
                            setPage(Pages.Applications);
                        }}>
                        <i className="fa-solid fa-circle-user me-1"></i> Заявки
                    </a>
                </div>
                <div
                    className={`${role !== Role.Admin && 'd-none'} px-lg-3 flex-fill text-center d-flex align-items-center`}>
                    <a
                        className={`${styles.element} text-black-50 ${!isPhone && "px-4 py-2 fs-6"} ${isPhone && "px-2 py-1"} text-uppercase ${currentPage === Pages.Subscription ? styles.active : ''}`}
                        href=""
                        onClick={(event) => {
                            event.preventDefault();
                            setPage(Pages.Subscription);
                        }}>
                        <i className="fa-solid fa-circle-user me-1"></i> Абонементы
                    </a>
                </div>
                <div
                    className={`${(!isAuthenticated || role === Role.User) && 'd-none'} px-lg-3 flex-fill text-center d-flex align-items-center`}>
                    <a
                        className={`${styles.element} text-black-50 ${!isPhone && "px-4 py-2 fs-6"} ${isPhone && "px-2 py-1"} text-uppercase ${currentPage === Pages.Search ? styles.active : ''}`}
                        href=""
                        onClick={(event) => {
                            event.preventDefault();
                            setPage(Pages.Search);
                        }}>
                        <i className="fa-solid fa-circle-user me-1"></i> Поиск
                    </a>
                </div>
                <div
                    className={`${(role !== Role.Trainer) && 'd-none'} px-lg-3 flex-fill text-center d-flex align-items-center`}>
                    <a
                        className={`${styles.element} text-black-50 ${!isPhone && "px-4 py-2 fs-6"} ${isPhone && "px-2 py-1"} text-uppercase ${currentPage === Pages.CreateTraining ? styles.active : ''}`}
                        href=""
                        onClick={(event) => {
                            event.preventDefault();
                            setPage(Pages.CreateTraining);
                        }}>
                        <i className="fa-solid fa-circle-user me-1"></i> Создать тренировку
                    </a>
                </div>
                <div
                    className={`${(role !== Role.Trainer) && 'd-none'} px-lg-3 flex-fill text-center d-flex align-items-center`}>
                    <a
                        className={`${styles.element} text-black-50 ${!isPhone && "px-4 py-2 fs-6"} ${isPhone && "px-2 py-1"} text-uppercase ${currentPage === Pages.Schedule ? styles.active : ''}`}
                        href=""
                        onClick={(event) => {
                            event.preventDefault();
                            setPage(Pages.Schedule);
                        }}>
                        <i className="fa-solid fa-circle-user me-1"></i> Расписание
                    </a>
                </div>
                <div
                    className={`${(role !== Role.User) && 'd-none'} px-lg-3 flex-fill text-center d-flex align-items-center`}>
                    <a
                        className={`${styles.element} text-black-50 ${!isPhone && "px-4 py-2 fs-6"} ${isPhone && "px-2 py-1"} text-uppercase ${currentPage === Pages.ClientSchedule ? styles.active : ''}`}
                        href=""
                        onClick={(event) => {
                            event.preventDefault();
                            setPage(Pages.ClientSchedule);
                        }}>
                        <i className="fa-solid fa-circle-user me-1"></i> Расписание
                    </a>
                </div>
                <div
                    className={`${(role !== Role.User) && 'd-none'} px-lg-3 flex-fill text-center d-flex align-items-center`}>
                    <a
                        className={`${styles.element} text-black-50 ${!isPhone && "px-4 py-2 fs-6"} ${isPhone && "px-2 py-1"} text-uppercase ${currentPage === Pages.ClientBooking ? styles.active : ''}`}
                        href=""
                        onClick={(event) => {
                            event.preventDefault();
                            setPage(Pages.ClientBooking);
                        }}>
                        <i className="fa-solid fa-circle-user me-1"></i> Запись
                    </a>
                </div>
                <div
                    className={`${!isAuthenticated && 'd-none'} px-lg-3 flex-fill text-center d-flex align-items-center`}>
                    <a
                        className={`${styles.element} text-black-50 ${!isPhone && "px-4 py-2 fs-6"} ${isPhone && "px-2 py-1"} text-uppercase ${currentPage === Pages.Notification ? styles.active : ''}`}
                        href=""
                        onClick={(event) => {
                            event.preventDefault();
                            setPage(Pages.Notification);
                        }}>
                        <i className="fa-solid fa-circle-user me-1"></i> Уведомления
                    </a>
                </div>

            </div>

            {!isPhone && <div className="flex-grow-1 mx-3"></div>}
        </div>
    );
};

export default Navbar;