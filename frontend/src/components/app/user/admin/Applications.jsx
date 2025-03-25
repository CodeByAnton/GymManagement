import { useEffect, useState } from 'react';
import { MDBBtn } from "mdb-react-ui-kit";
import { APPLICATIONS, CONSIDER_APPLICATION, ApplicationDecisions } from "../../../../tools/consts";
import styles from '../../../../css/Applications.module.css';

const Applications = () => {
    const [applications, setApplications] = useState([]);
    const [refresh, setRefresh] = useState(true);

    useEffect(() => {
        const accessToken = localStorage.getItem("accessToken");
        fetch(APPLICATIONS, { headers: { "Authorization": `Bearer ${accessToken}` } })
            .then(response => response.json())
            .then(data => setApplications(data))
            .catch(error => console.error("Ошибка при загрузке заявок:", error));
    }, [refresh]);

    const sendDecision = (id, decision) => {
        const accessToken = localStorage.getItem("accessToken");
        fetch(CONSIDER_APPLICATION, {
            method: "POST", body: JSON.stringify({ applicationId: id, newStatus: decision}),
            headers: { "Content-Type": "application/json", "Authorization": `Bearer ${accessToken}` },
        })
            .then(response => {
                if (response.ok) {
                    console.log('Заявка успешно одобрена');
                    setRefresh(!refresh);
                } else {
                    throw new Error(response.statusText);
                }
            })
            .catch(error => console.error("Не удалось одобрить заявку"));
    }
    const handleAccept = (id) => {
        sendDecision(id, ApplicationDecisions.ACCEPT);
    };

    const handleReject = (id) => {
        sendDecision(id, ApplicationDecisions.REJECT);
    };

    const backgroundImageUrl = 'url(/img/sea.jpg)';

    const backgroundStyle = {
        backgroundImage: backgroundImageUrl,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        height: '93vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
    };

    return (
        <div style={backgroundStyle}>
            <div className={styles.tableContainer} style={{paddingBottom: '3%', borderRadius: '12px'}}>
                <h2 className="text-center" style={{ marginBottom: '3%' }}>Список заявок на получение прав администратора</h2>
                <table className={styles.table}>
                    <thead>
                    <tr>
                        <th className="text-center">Имя пользователя</th>
                        <th className="text-center">Имя</th>
                        <th className="text-center">Фамилия</th>
                        <th className="text-center">Роль</th>
                        <th className="text-center">Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {applications.length > 0 ? (
                        applications.map(application => (
                            <tr key={application.id}>
                                <td className="text-center">{application.email}</td>
                                <td className="text-center">{application.name}</td>
                                <td className="text-center">{application.surname}</td>
                                <td className="text-center">{application.desiredRole}</td>
                                <td className="text-center">
                                    <MDBBtn color="success" size="sm" className="me-2"
                                            onClick={() => handleAccept(application.id)}>Принять</MDBBtn>
                                    <MDBBtn color="danger" size="sm"
                                            onClick={() => handleReject(application.id)}>Отклонить</MDBBtn>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td className="text-center">—</td>
                            <td className="text-center">—</td>
                            <td className="text-center">—</td>
                            <td className="text-center">—</td>
                            <td className="text-center">—</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Applications;