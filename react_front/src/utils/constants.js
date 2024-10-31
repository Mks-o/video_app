import { comment_page_li, comment_page_ul } from "./bootstrap_styles";

export const menu = ['Rate videos', 'All users', 'All videos', 'All comments', 'My videos', 'Add video', 'Logout']

export const base_url = 'http://localhost:8080/';

export const login_url = 'registration/login';
export const registration = 'registration/';
export const authorization = 'authenticated/auth';
export const get_user_by_id = 'user/get/';
export const update_user = '/user/updateuser';

export const add_video = 'video/add';
export const rate_video = 'video/update'
export const delete_video = 'video/delete/';
export const delete_user = 'user/delete/';

export const add_comment = 'video/addcomment';
export const delete_comment_by_id = 'comments/delete/'
export const get_next_video = 'video/get/nextvideo';

export const get_video_by_id = 'video/get/video/';
export const get_comments_by_video_id = 'video/getcomments/';

export const get_all_comments = 'comments/getall';
export const get_all_users = 'user/getusers';
export const get_all_videos = 'video/get/videos';

export const get_random_videos = 'video/get/randomvideos';
export const get_popular_videos = 'video/get/popularvideos';

export const get_my_videos = 'video/get/uservideo/';

export const set_headers = (auth) => {
    let headers = {
        "Accept": "*/*",
        "Accept-Encoding": "gzip, deflate, br",
        "Connection": "keep-alive",
        "Content-Type": "application/json",
        "Cache-Control": "no-cache"
    }
    if (auth != null) headers.Authorization = auth
    return headers;
}

export const create_element = (name, ref, input_type) => {
    return <div className='input-group'>
        <label className='input-group-text bg-secondary text-light'>{name}</label>
        <input className='form-control' type={input_type} ref={ref} name={name} placeholder={name}></input>
    </div>
}
export const create_label_element = (name, content) => {
    return <div className='input-group m-1'>
        <label className=' input-group-text bg-secondary text-light'>{name}</label>
        <label className='form-control bg-dark text-light' placeholder={name}>{content}</label>
    </div>
}
export const navigation = (index, array, nextpageMethod) => {
    if (array.length === 0) return <div class="spinner-border"></div>;
    let previous = index <= 0 ? "" : index;
    let current = index + 1;
    let next = index + 2 > array.length ? "" : index + 2;
    return <ul className={comment_page_ul}>
        <li className="page-item"><button className={comment_page_li}
            onClick={() => nextpageMethod(index - 1)}>Previous {previous}</button></li>
        <li className="page-item"><button className={comment_page_li}>Current page {current}  of {array.length}</button></li>
        <li className="page-item"><button className={comment_page_li}
            onClick={() => nextpageMethod(index + 1)}>Next {next}</button></li>
    </ul>
}