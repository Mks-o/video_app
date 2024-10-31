import { base_url, get_all_videos, navigation, set_headers } from '../../utils/constants';
import VideoElement from '../elements/VideoElement';
import { videoContext } from '../../utils/videoContext';
import React, { useContext, useState } from 'react';
import { users_page_style } from '../../utils/bootstrap_styles';

const VideosPage = () => {
    const { user, fetchFunc } = useContext(videoContext);
    const all_videos_url = base_url + get_all_videos;
    const [video_data, setVideosData] = useState([]);
    const [videos, setArrays] = useState({ index: 0, pages: [] });
    const loadData = async () => {
        const parameters = {
            headers: set_headers(`Bearer ${user.token}`),
            method: "GET"
        }

        let data = await fetchFunc(all_videos_url, parameters);
        if (video_data.length === 0)
            setVideosData(data);
        if (videos.pages.length === 0)
            changeContent(0);
    }
    const changeContent = (i) => {
        const pages = [];
        const countOnPage = 10;
        for (let i = 0; i < video_data.length; i += countOnPage) {
            pages.push(video_data.slice(i, i + countOnPage));
        }
        setArrays({ index: i <= 0 ? 0 : i > videos.pages.length - 1 ? videos.pages.length - 1 : i, pages: pages });
    }
    loadData();
    return (
        <div className={users_page_style}>
            {navigation(videos.index, videos.pages, changeContent)}
            <nav className='row p-0 m-0'>
                {videos.pages[videos.index] === null ? 'No content' : videos.pages[videos.index]?.map((video, index) => {
                    return <VideoElement
                        data={video}
                        rate={null}
                        add_comment={null}
                        key={index} />
                })}
            </nav>
        </div>
    );
}

export default VideosPage;

