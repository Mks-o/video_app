import { add_comment, base_url, create_label_element, get_my_videos, set_headers, delete_video, rate_video, delete_user, delete_comment_by_id, get_user_by_id, navigation } from '../../utils/constants';
import VideoElement from '../elements/VideoElement';
import { videoContext } from '../../utils/videoContext';
import React, { useContext, useState } from 'react';
import { my_videos_body_style, my_videos_delete_style, my_videos_header_style, my_videos_page_style } from '../../utils/bootstrap_styles';

const MyVideosPage = () => {
    const { user, fetchFunc, setValues } = useContext(videoContext);
    const url = base_url + get_my_videos + user.id;
    const add_comment_url = base_url + add_comment;

    const [video_data, setVideoData] = useState({ videos: [], comments: [] });

    const [videos, setVideosPages] = useState({ index: 0, pages: [] });

    const [comments, setCommentsPages] = useState({ index: 0, pages: [] });

    const getMyVideos = async () => {
        if (videos.pages.length !== 0 || comments.pages.length !== 0) return;
        const parameters = {
            headers: set_headers(`Bearer ${user.token}`),
            method: "GET"
        }
        let data = await fetchFunc(url, parameters);
        let uservalues = await fetchFunc(base_url + get_user_by_id + user.id, parameters);
        if (data.length === 0 && uservalues.comments.length === 0) return;
        setVideoData({ videos: data, comments: uservalues.comments });
        changeVideoContent(0);
        changeCommentsContent(0);
    }
    const addComent = async (video_id, comment) => {
        const comment_data = {
            "content": comment,
            "ownerId": user.id,
            "videoId": video_id,
        };
        const parameters = {
            method: 'POST',
            body: JSON.stringify(comment_data),
            headers: set_headers(`Bearer ${user.token}`)
        }
        await fetch(add_comment_url, parameters);
    }
    const deleteVideo = async (video_id, index) => {
        const parameters = {
            method: 'DELETE',
            headers: set_headers(`Bearer ${user.token}`)
        }
        await fetch(base_url + delete_video + video_id, parameters);
        let video = video_data.videos.find(c => c.id === video_id);
        let video_index = video_data.videos.indexOf(video);
        video_data.videos.splice(video_index, 1);
        changeVideoContent(0);//setVideoData({ videos: [], comments: [] });
    }
    const updateVideo = async (video, title, description, close_update_elements) => {
        let new_data = {
            "id": video.id,
            "title": title,
            "description": description,
            "raiting": video.raiting,
            "comments": video.comments
        }
        const parameters = {
            method: 'POST',
            headers: set_headers(`Bearer ${user.token}`),
            body: JSON.stringify(new_data)
        }
        await fetch(base_url + rate_video, parameters)
        let current_video = video_data.videos.find(c => c.id === video.id);
        let video_index = video_data.videos.indexOf(current_video);
        video_data.videos[video_index].title = title;
        video_data.videos[video_index].description = description;
        close_update_elements();
        changeVideoContent(0);
    }
    const deleteAccount = async () => {
        const parameters = {
            method: 'DELETE',
            headers: set_headers(`Bearer ${user.token}`)
        }

        await fetch(base_url + delete_user + user.id, parameters);
        setValues({
            user: '',
            currentPage: 'Login'
        })
    }
    const deleteComment = async (e, id) => {
        e.preventDefault();
        const parameters = {
            method: 'DELETE',
            headers: set_headers(`Bearer ${user.token}`)
        }
        await fetch(base_url + delete_comment_by_id + id, parameters);
        let c = video_data.comments.find(c => c.id === id);
        let comment_index = video_data.comments.indexOf(c);
        video_data.comments.splice(comment_index, 1);
        changeCommentsContent(0);
    }
    const changeVideoContent = (index) => {
        const pages = [];
        const countOnPage = 10;
        for (let i = 0; i < video_data.videos.length; i += countOnPage) {
            pages.push(video_data.videos.slice(i, i + countOnPage));
        }
        let currentIndex = index <= 0 ? 0 : index > videos.pages.length - 1 ? videos.pages.length - 1 : index;
        setVideosPages({ index: currentIndex, pages: pages });
    }
    const changeCommentsContent = (index) => {
        const pages = [];
        const countOnPage = 10;
        for (let i = 0; i < video_data.comments.length; i += countOnPage) {
            pages.push(video_data.comments.slice(i, i + countOnPage));
        }
        let currentIndex = index <= 0 ? 0 : index > comments.pages.length - 1 ? comments.pages.length - 1 : index;
        setCommentsPages({ index: currentIndex, pages: pages });
    }
    getMyVideos();
    return (
        <section className={my_videos_page_style}>
            <section className={my_videos_header_style}>
                <p>Info</p>
                {create_label_element('Name:', user.name)}
                {create_label_element('Login:', user.login)}
                {create_label_element('Birthdate:', user.birthdate)}
                {create_label_element('Mail:', user.mail)}
                {create_label_element('Raiting:', user.raiting)}
                {create_label_element('Role:', user.role)}
                <button className={my_videos_delete_style} onClick={() => deleteAccount()}>Delete account</button>
            </section>
            <section className={my_videos_body_style}>
                {navigation(videos.index, videos.pages, changeVideoContent)}
                {videos.pages[videos.index]?.length === 0 ? 'No content' : videos.pages[videos.index]?.map((video, index) => {
                    return <VideoElement
                        data={video}
                        rate={null}
                        add_comment={addComent}
                        delete_video={deleteVideo}
                        update_video={updateVideo}
                        key={index} />
                })}
            </section>
            <section className='card-footer'>
                {navigation(comments.index, comments.pages, changeCommentsContent)}
                <div>My comments
                    {comments.pages[comments.index]?.length === 0 ?
                        "No content" :
                        comments.pages[comments.index]?.map((comment, index) => {
                            return <div className='card p-0 m-0' key={index}>{user.name}: {comment.content}
                                <button className='badge bg-danger' onClick={(e) => deleteComment(e, comment.id)}>X</button>
                            </div>
                        })}
                </div>
            </section>
        </section>
    );
}

export default MyVideosPage;

