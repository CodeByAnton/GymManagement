import React, {useState} from 'react';
import {Pages} from "../../../tools/consts";
import Navbar from "../user/common/Navbar";
import AuthenticationProvider from "../../auth/providers/AuthenticationProvider";
import {useSelector} from "react-redux";
import {selectAuthenticated} from "../../../redux/selectors";
import Applications from "../user/admin/Applications";
import SubscriptionProvider from "../../auth/providers/SubscriptionProvider";
import Notifications from "../user/common/Notifications";
import Profile from "../user/common/Profile";
import FindUserInfoByNameAndSurname from "../user/trainer/FindUserInfoByNameAndSurname";
import CreateTrainingProvider from "../../auth/providers/CreateTrainingProvider";
import Schedule from "../user/trainer/Schedule";
import GroupTrainingBooking from "../user/client/GroupTrainingBooking";
import ClientSchedule from "../user/client/ClientSchedule";

const UserPage = () => {

    const [page, setPage] = useState(Pages.Login);

    const authenticated = useSelector(selectAuthenticated);
    return (
        <>
            <Navbar setPage={setPage} currentPage={page}/>
            {
                !authenticated && page === Pages.Login &&
                <AuthenticationProvider
                    isAdmin={false}
                />
            }
            {
                authenticated && page === Pages.Login &&
                <Profile/>
            }
            {
                authenticated && page === Pages.Applications &&
                <Applications/>
            }
            {
                authenticated && page === Pages.Subscription &&
                <SubscriptionProvider/>
            }
            {
                authenticated && page === Pages.Notification &&
                <Notifications/>
            }
            {
                authenticated && page === Pages.Search &&
                <FindUserInfoByNameAndSurname/>
            }
            {
                authenticated && page === Pages.CreateTraining &&
                <CreateTrainingProvider/>
            }
            {
                authenticated && page === Pages.Schedule &&
                <Schedule/>
            }
            {
                authenticated && page === Pages.ClientBooking &&
                <GroupTrainingBooking/>
            }
            {
                authenticated && page === Pages.ClientSchedule &&
                <ClientSchedule/>
            }
        </>
    );
};

export default UserPage;