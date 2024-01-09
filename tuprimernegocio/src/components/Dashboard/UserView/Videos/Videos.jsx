import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchVideosByUser, markVideoAsWatched, unmarkVideoAsWatched } from '../../../../store/actions/authActions';
import ReactPlayer from 'react-player';
import '../Videos/Videos.css';

const Videos = () => {
  const dispatch = useDispatch();
  const videosData = useSelector((state) => state.auth.videos);
  const [selectedVideo, setSelectedVideo] = useState(null);
  const [watchedVideos, setWatchedVideos] = useState({});
  const [accordionState, setAccordionState] = useState({});

  useEffect(() => {
    dispatch(fetchVideosByUser());
  }, [dispatch]);

  // Revisar el video seleccionado y los IDs de los videos
  useEffect(() => {
    console.log("Selected Video:", selectedVideo);
    if (videosData) {
      videosData.forEach(section => {
        section.videos.forEach(video => {
          console.log("Video in Data:", video);
        });
      });
    }
  }, [videosData, selectedVideo]);

  useEffect(() => {
    if (videosData && videosData.length > 0) {
      const newWatchedVideos = {};
      videosData.forEach(section => {
        if (section.videos) {
          section.videos.forEach(video => {
            newWatchedVideos[video.id] = video.is_watched === 1;
          });
        }
      });
      setWatchedVideos(newWatchedVideos);
    }
  }, [videosData]);

  useEffect(() => {
    if (!selectedVideo && videosData && videosData.length > 0 && videosData[0].videos && videosData[0].videos.length > 0) {
      const firstVideo = videosData[0].videos[0];
      setSelectedVideo(firstVideo);
    }
  }, [videosData, selectedVideo]);

  const markAsWatched = async (videoId) => {
    try {
      await dispatch(markVideoAsWatched(videoId));
      setWatchedVideos((prevState) => ({ ...prevState, [videoId]: true }));
      dispatch(fetchVideosByUser()); // Volver a cargar los datos actualizados
      advanceToNextVideo();
    } catch (error) {
      console.error("Error al marcar el video como visto:", error);
    }
  };
  
  const unmarkAsWatched = async (videoId) => {
    try {
      await dispatch(unmarkVideoAsWatched(videoId));
      setWatchedVideos((prevState) => ({ ...prevState, [videoId]: false }));
      dispatch(fetchVideosByUser()); // Volver a cargar los datos actualizados
    } catch (error) {
      console.error("Error al desmarcar el video como visto:", error);
    }
  };
  


  const advanceToNextVideo = () => {
    if (!selectedVideo || !videosData) return;

    const currentSectionIndex = videosData.findIndex((section) =>
      section.videos && section.videos.some((video) => video.id === selectedVideo.id)
    );

    if (currentSectionIndex === -1) return;

    const currentSection = videosData[currentSectionIndex];
    const currentIndex = currentSection.videos.findIndex((video) => video.id === selectedVideo.id);

    let nextVideo = null;

    // Buscar el siguiente video en la sección actual
    if (currentIndex < currentSection.videos.length - 1) {
      nextVideo = currentSection.videos[currentIndex + 1];
    }

    // Si no hay más videos en la sección actual, buscar en la siguiente sección
    if (!nextVideo) {
      for (let i = (currentSectionIndex + 1) % videosData.length; i !== currentSectionIndex; i = (i + 1) % videosData.length) {
        if (videosData[i].videos && videosData[i].videos.length > 0) {
          nextVideo = videosData[i].videos[0];
          break;
        }
      }
    }

    // Si se encuentra un siguiente video, actualizar el video seleccionado
    if (nextVideo) {
      setSelectedVideo(nextVideo);
      setAccordionState((prevState) => ({
        ...prevState,
        [currentSection.section_id]: true,
      }));
    }
  };


  const handleAccordionClick = (sectionId) => {
    setAccordionState((prevState) => ({
      ...prevState,
      [sectionId]: !prevState[sectionId],
    }));
  };

  return (
    <div className="videos-container">
      <div className="main-content">
        {selectedVideo ? (
          <>
            <div className="video-header">
              <h3>{selectedVideo.title}</h3>
            </div>
            <ReactPlayer
              url={selectedVideo.url}
              controls
              width="145vh"
              height="65vh"
              onEnded={advanceToNextVideo}
            />
            <div className="video-content">
              <p>{selectedVideo.content}</p>
              {!watchedVideos[selectedVideo.id] ? (
                <div className="mark-as-watched">
                  <button onClick={() => markAsWatched(selectedVideo.id)}>
                    Marcar como visto y continuar con el siguiente video
                  </button>
                </div>
              ) : (
                <div className="mark-as-watched">
                  <button onClick={() => unmarkAsWatched(selectedVideo.id)}>
                    Desmarcar como visto y continuar con el siguiente video
                  </button>
                </div>
              )}

            </div>
          </>
        ) : (
          <p>No hay un video seleccionado</p>
        )}
      </div>
      <div className="SideBarVideos">
        {videosData && videosData.length > 0 ? (
          videosData.map((section) => (
            <div key={section.section_id}>
              <button
                onClick={() => handleAccordionClick(section.section_id)}
                className={accordionState[section.section_id] ? 'active-section' : ''}
              >
                <div className="sequence-box">{section.section_sequence_number}</div>
                {section.section_name}
              </button>
              {accordionState[section.section_id] && section.videos && section.videos.length > 0 ? (
                section.videos.map((video) => (
                  <div
                    key={video.id}
                    onClick={() => setSelectedVideo(video)}
                    className="video-item"
                  >
                    <div className="sequence-box">{video.sequence_number}</div>
                    <div className="video-thumbnail" style={{ backgroundImage: `url(${video.url})` }}>
                    </div>
                    <span>{video.title}</span>
                    {watchedVideos[video.id] && <div className="watched-check checked">✔</div>}
                  </div>
                ))
              ) : (
                accordionState[section.section_id] && <p>El administrador no ha agregado videos en esta sección aún.</p>
              )}
            </div>
          ))
        ) : (
          <p>No se ha agregado ninguna sección aún</p>
        )}
      </div>
    </div>
  );
};

export default Videos;