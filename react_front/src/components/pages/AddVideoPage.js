import React, { useContext, useState } from 'react';
import { videoContext } from '../../utils/videoContext';
import { add_video, base_url, set_headers,create_element } from '../../utils/constants';
import { add_video_page, btn_success } from '../../utils/bootstrap_styles';

const AddVideoPage = () => {
    const { user,fetchFunc } = useContext(videoContext);
    const add_video_url = base_url + add_video;
    const video_url_ref = React.createRef();
    const title_ref = React.createRef();
    const description_ref = React.createRef();
    const [status,setStatus] = useState('');
    const addVideo = async(event) => {
        event.preventDefault();
        const data = {
            "id": null,
            "title": title_ref.current?.value,
            "description": description_ref.current?.value,
            "url": video_url_ref.current?.value,
            "ownerId": user.id,
            "raiting": 0,
            "comments": null
        }
        const parameters = {
            method: 'POST',
            body: JSON.stringify(data),
            headers: set_headers(`Bearer ${user.token}`)
        }
        await fetchFunc(add_video_url,parameters);
        setStatus('video was added')
    }
    return (
        <form className={add_video_page}>
            {create_element('Title',title_ref,'text')}
            {create_element('Descripton',description_ref,'text')}
            {create_element('Video url',video_url_ref,'url')}
            <button className={btn_success} onClick={(event)=>addVideo(event)}>Add video</button>
            <label>{status}</label>
        </form>
    );
}

export default AddVideoPage;
