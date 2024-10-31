import './App.css';
import Login from './components/pages/Login';
import React, { useState } from 'react';
import { videoContext } from './utils/videoContext';
import MainPage from './components/pages/MainPage';
import { menu } from './utils/constants';
import UsersPage from './components/pages/UsersPage';
import VideosPage from './components/pages/VideosPage';
import CommentsPage from './components/pages/CommentsPage';
import MyVideosPage from './components/pages/MyVideosPage';
import AddVideoPage from './components/pages/AddVideoPage';
import Registration from './components/pages/Registration';
import { app_style, menu_btn } from './utils/bootstrap_styles';

const App = () => {
  const [pageValues, setValues] = useState({
    user: '',
    currentPage: 'Login'
  });
  const changePage = (page) => {
    setValues({
      user: pageValues.user,
      currentPage: page
    })
  }
  const fetchFunc = async (url, parametrs) => {
    console.log(url);

    let data = await fetch(url, parametrs)
      .then(res => {
        if (res.status === 504) {
          prompt("token time is out", res.status);
          return changePage('Login');
        }
        if (res.status !== 200) {
          console.error(res.status);
          return res = [res.json().body];
        }
        else {
          return res.json();
        }
      })
      .then(result => result)
      .catch(reason => console.error(reason.body));
    return data;
  }
  const showContent = () => {
    const menu_element = menu.map((menuItem, index) => {
      return <button className={menu_btn} onClick={() => changePage(menuItem)} key={index}>{menuItem}</button>
    })
    switch (pageValues.currentPage) {
      case 'Login':
        return <Login key={9} />
      case 'Register':
        return <Registration key={10} />
      case 'Main page':
        return [menu_element, <MainPage key={11} />]
      case 'All users':
        return [menu_element, <UsersPage key={12} />]
      case 'All videos':
        return [menu_element, <VideosPage key={13} />]
      case 'All comments':
        return [menu_element, <CommentsPage key={14} />]
      case 'My videos':
        return [menu_element, <MyVideosPage key={15} />]
      case 'Add video':
        return [menu_element, <AddVideoPage key={16} />]
      case 'Logout':
        return setValues({
          user: '',
          currentPage: 'Login'
        })
      default:
        return [menu_element, <MainPage />];
    }
  }
  return (
    <div className={app_style}>
      <videoContext.Provider value={{
        user: pageValues.user,
        setValues,
        changePage,
        fetchFunc
      }}>
        {showContent()}
      </videoContext.Provider>
    </div>
  );
}

export default App;
