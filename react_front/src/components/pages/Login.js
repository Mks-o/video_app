import React, { useContext } from 'react';
import { videoContext } from '../../utils/videoContext';
import { base_url, login_url, set_headers,authorization, create_element  } from '../../utils/constants'
import { login_btn_style, login_group_style, login_page_style, login_register_style } from '../../utils/bootstrap_styles';

const Login = () => {
    const { changePage, setValues, fetchFunc } = useContext(videoContext);
    let user_name = React.createRef();
    let user_password = React.createRef();
    const url = base_url + login_url;
    const auth_url = base_url + authorization;

    const setUser = async () => {
        const userValues = {
            "username": user_name?.current.value,
            "password": user_password?.current.value
        }
        const parameters = {
            headers: set_headers(null),
            method: "POST",
            body: JSON.stringify(userValues)
        }
        let auth = await fetchFunc(auth_url,parameters);
        let userData = await fetchFunc(url, parameters);
        if (auth.length === 0) return;
        userData.token = auth.token;
        setValues({
            user: userData,
            currentPage: "Main page"
        })
    }

    return (
        <div className={login_page_style}>
        {create_element('Login',user_name,'text')}
        {create_element('Password',user_password,'password')}
            <div className={login_group_style}>
            <button className={login_btn_style} onClick={() => setUser()}>Login</button>
            <button className={login_register_style} onClick={() => changePage('Register')}>Register</button>

            </div>
            <label></label>
        </div>
    );
}

export default Login;
