import ReactDOM from 'react-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'mdb-react-ui-kit/dist/css/mdb.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import './css/main.css'
import store from './redux/store';
import App from './App';
import {Provider} from "react-redux";


ReactDOM.render(<Provider store={store}>
    <App/>
</Provider>, document.getElementById('root'))


