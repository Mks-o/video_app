import React, { useContext, useState } from 'react';
import style from './video.style.module.css';
import { create_label_element } from '../../utils/constants';
import { videoContext } from '../../utils/videoContext';
import { create_element } from '../../utils/constants';
import { video_element_card_body_style, video_element_card_header_style, video_element_style } from '../../utils/bootstrap_styles';
const VideoElement = ({ data, rate, add_comment, delete_video, update_video }) => {
    const [comment, setComent] = useState('')
    const [update_elements, setUpdateElements] = useState('')
    const comment_ref = React.createRef();
    const title_ref = React.createRef();
    const description_ref = React.createRef();
    const { user } = useContext(videoContext);
    const fillComments = () => {
        return <ol className="list-group list-group-flush text-light">Comments:
            {data.comments.map((comment, index) => {
                return <ul className='bg-secondary list-group-item rounded border border-light text-light' key={index}>{comment.content}</ul>
            })}
        </ol>
    }
    const showcomment = () => {
        setComent(comment === '' ? <div>
            <textarea className='w-100 bg-secondary text-light border border-light' type='text' ref={comment_ref}></textarea>
            <button className="btn btn-success border border-light" onClick={() => {
                add_comment(data.id, comment_ref.current.value)
                data.comments.push({ content: comment_ref.current.value })
                //fillComments();
                setComent('');
            }}>Submit</button>
        </div> : '')
    }
    const showUpdateElements = () => {
        setUpdateElements(update_elements === '' ? <div>
            {create_element('New title:', title_ref, 'text')}
            {create_element('New description:', description_ref, 'text')}
            <button className="btn btn-success border border-light" onClick={() =>
                update_video(data, title_ref.current.value, description_ref.current.value, setUpdateElements)}>Submit</button>
        </div> : '')
    }
    return (
        <div className={video_element_style}>
            <section className={video_element_card_header_style}>
                {create_label_element('Title', data.title)}
                <div className={'input-group m-1 '}>
                    {data.ownerId === user.id && rate === null &&
                        <button className={'btn btn-danger border form-control'} onClick={() => delete_video(data.id)}>Delete video</button>}
                    {data.ownerId === user.id && rate === null &&
                        <button className={'btn btn-warning border form-control'} onClick={() => showUpdateElements()}>Update video</button>}
                </div>
                {update_elements}
            </section>
            <section className={video_element_card_body_style}>
                {add_comment !== null && <iframe className='border bg-dark rounded'
                    style={style}
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                    referrerPolicy="strict-origin-when-cross-origin" allowFullScreen
                    title='unic'
                    src={data.url.replace("watch?v=", "embed/")}></iframe>
                }
                <label>Raiting: {data.raiting}</label>
                {create_label_element('Description', data.description)}
                <div>
                    {rate !== null &&
                        <button className='btn btn-warning border ' onClick={() => rate(data)}>Rate</button>}
                    {add_comment !== null &&
                        <button className='btn btn-success  border border-light' onClick={() => showcomment()}>Add comment</button>}
                </div>
                {add_comment !== null ? comment : ''}
            </section>
            <section className='card-footer p-0 m-0 row'>
                <ol>{add_comment !== null ? fillComments() : <div className='btn btn-secondary'>Comments count: {data.comments.length}</div>}</ol>
            </section>
        </div>
    );
}

export default VideoElement;
