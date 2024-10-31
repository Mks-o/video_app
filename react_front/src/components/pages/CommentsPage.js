import { comment_page, comment_style } from '../../utils/bootstrap_styles';
import { base_url, get_all_comments, navigation } from '../../utils/constants';
import { videoContext } from '../../utils/videoContext';
import React, { useContext, useState } from 'react';

const CommentsPage = () => {
    const { user, fetchFunc } = useContext(videoContext);
    const all_comments_url = base_url + get_all_comments;
    const [comments_data, setCommentsData] = useState([]);
    const [arrays, setArrays] = useState({ index: 0, pages: [] });

    const loadData = async () => {
        const parameters = {
            headers: {
                "Accept": "*/*",
                "Accept-Encoding": "gzip, deflate, br",
                "Connection": "keep-alive",
                "Content-Type": "application/json",
                "Cache-control": "no-cache",
                "Authorization": `Bearer ${user.token}`,
            },
            method: "GET"
        }

        let data = await fetchFunc(all_comments_url, parameters);
        if (comments_data.length === 0) {
            setCommentsData(data);
        }

        if (arrays.pages.length === 0)
            changeContent(0);
    }
    const changeContent = (index) => {
        const pages = [];
        const countOnPage = 30;
        for (let i = 0; i < comments_data.length; i += countOnPage) {
            pages.push(comments_data.slice(i, i + countOnPage));
        }
        setArrays({ index: index <= 0 ? 0 : index > arrays.pages.length - 1 ? arrays.pages.length - 1 : index, pages: pages });
    }
    loadData();
    return (
        <div className={comment_page}>
            {navigation(arrays.index, arrays.pages, changeContent)}
            <nav className='row p-0 m-0'>
                {arrays.pages[arrays.index] === null ? 'No content' : arrays.pages[arrays.index]?.map((comment, index) => {
                    return <li
                        className={comment_style}
                        key={index}>
                        <p>{comment.content}</p>
                    </li>
                })}
            </nav>
        </div>
    );
}

export default CommentsPage;

