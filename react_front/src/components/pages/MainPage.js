import { add_comment, base_url, get_random_videos, rate_video, set_headers, update_user } from '../../utils/constants';
import VideoElement from '../elements/VideoElement';
import { videoContext } from '../../utils/videoContext';
import React, { useContext, useState } from 'react';
import { main_page_section_style, main_page_style } from '../../utils/bootstrap_styles';

const MainPage = () => {
    const { user, fetchFunc } = useContext(videoContext);
    const url = base_url + get_random_videos;
    const rate_url = base_url + rate_video;
    const add_comment_url = base_url + add_comment;
    const [video_data, setVideoData] = useState([]);

    const getRandomVideos = async () => {
        const parameters = {
            method: "GET",
            headers: set_headers(`Bearer ${user.token}`)

        }
        let data = await fetchFunc(url, parameters);
        if (video_data.length === 0)
            setVideoData(data);
    }
    const rateVideo = async (video) => {
        video.raiting += 1;
        const parameters = {
            method: 'POST',
            body: JSON.stringify(video),
            headers: set_headers(`Bearer ${user.token}`)
        }
        await fetchFunc(rate_url, parameters);
        let user_values = user;
        user_values.raiting+=1;
        //user.raiting+=1;
        parameters.body = user_values;
        await fetchFunc(base_url+update_user, parameters);

        setVideoData([]);
    }
    const addComent = async (video_id, comment) => {
        const comment_data = {
            "id": null,
            "content": comment,
            "ownerId": user.id,
            "videoId": video_id,
        };
        const parameters = {
            method: 'POST',
            body: JSON.stringify(comment_data),
            headers: set_headers(`Bearer ${user.token}`)
        }
        await fetch(add_comment_url, parameters).catch(reason => console.error(reason));
    }
    getRandomVideos();
    return (
        <div>
        <label className={main_page_style}>My raiting: {user.raiting}</label>
            <section className={main_page_section_style}>
                {video_data.length === 0 ? '' : video_data.map((video, index) => {
                    return <VideoElement
                        data={video}
                        rate={rateVideo}
                        add_comment={addComent}
                        key={index} />
                })}
            </section>            
        </div>
    );
}

export default MainPage;

