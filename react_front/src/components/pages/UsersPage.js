import { users_page_section_style, users_page_style } from '../../utils/bootstrap_styles';
import { base_url, create_label_element, get_all_users, set_headers } from '../../utils/constants';
import { videoContext } from '../../utils/videoContext';
import React, { useContext, useState } from 'react';

const UsersPage = () => {
    const { user, fetchFunc } = useContext(videoContext);
    const all_users_url = base_url + get_all_users;
    const [users_data, setUsersData] = useState([]);

    const loadData = async () => {
        const parameters = {
            headers: set_headers(`Bearer ${user.token}`),

            method: "GET"
        }

        let data = await fetchFunc(all_users_url, parameters);
        if (users_data.length === 0)
            setUsersData(data);
    }
    loadData();
    
    return (
        <div className={users_page_style}>
            {users_data.length === 0 ? '' : users_data.map((user, index) => {
                return <div
                    className={users_page_section_style}
                    key={index}>
                    {create_label_element('Name:', user.name)}
                    {create_label_element('Login:', user.login)}
                    {create_label_element('Birthdate:', user.birthdate)}
                    {create_label_element('Mail:', user.mail)}
                    {create_label_element('Raiting:', user.raiting)}
                    {create_label_element('Role:', user.role)}
                    {create_label_element('Comments count:', user.comments.length)}
                    {create_label_element('Videos count:', user.videos.length)}
                </div>
            })}
        </div>
    );
}

export default UsersPage;

