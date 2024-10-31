import React, { useContext } from 'react';
import { authorization, base_url, registration, set_headers } from '../../utils/constants';
import { videoContext } from '../../utils/videoContext';
import { create_element } from '../../utils/constants';
import { btn_danger, btn_success, registration_style } from '../../utils/bootstrap_styles';

const Registration = () => {
    const registration_url = base_url + registration;
    const auth_url = base_url + authorization;

    const name_ref = React.createRef();
    const login_ref = React.createRef();
    const mail_ref = React.createRef();
    const birthdate_ref = React.createRef();
    const password_ref = React.createRef();

    const { fetchFunc, setValues } = useContext(videoContext);
    const register = async (event) => {
        event.preventDefault();
        const userData = {
            "name": name_ref.current?.value,
            "login": login_ref.current?.value,
            "mail": mail_ref.current?.value,
            "role": "USER",
            "birthdate": birthdate_ref?.current.value,
            "raiting": 0,
            "videos": null,
            "comments": null
        }
        const parameters = {
            method: 'POST',
            body: JSON.stringify(userData),
            headers: set_headers(null)
        }

        const user = await fetchFunc(registration_url + password_ref.current.value, parameters);
        parameters.body = JSON.stringify({
            "username": userData.login,
            "password": password_ref.current?.value
        })
        let auth = await fetchFunc(auth_url, parameters);
        user.token = auth.token;
        if (auth.token)
            setValues({
                user: user,
                currentPage: 'Main page'
            })
    }

    return (
        <form className={registration_style}>
            {create_element('Name:', name_ref, 'text')}
            {create_element('Login:', login_ref, 'text')}
            {create_element('Password:', password_ref, 'password')}
            {create_element('Mail:', mail_ref, 'mail')}
            {create_element('Birthdate:', birthdate_ref, 'date')}
            <button className={btn_success} onClick={(event) => register(event)}>Registration</button>
            <button className={btn_danger} type='reset'>Clear</button>
        </form>
    );
}

export default Registration;
