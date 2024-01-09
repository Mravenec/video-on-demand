import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useForm } from 'react-hook-form';
import { AiOutlinePlus, AiOutlineEdit, AiOutlineDelete } from 'react-icons/ai'; // Importa los iconos
import { RiPencilLine } from 'react-icons/ri'; // Importa el ícono de lápiz para editar URL
import {
  fetchVideos,
  createNewSection,
  insertVideo,
  updateSectionSequence,
  updateVideoSequence,
  updateVideoTitle,
  updateSectionName,
  deactivateSection,
  deactivateVideo,
  updateVideoUrl, // Importa la acción para actualizar la URL del video
  updateVideoDescription, // Importa la acción para actualizar la descripción del video
} from '../../../../store/actions/authActions';
import ReactPlayer from 'react-player';
import './Videos.css';

const Videos = () => {
  const dispatch = useDispatch();
  const videosData = useSelector((state) => state.auth.videos);
  const [selectedVideo, setSelectedVideo] = useState(null);
  const [accordionState, setAccordionState] = useState({});
  const [watchedVideos, setWatchedVideos] = useState({});
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [sectionToAddVideo, setSectionToAddVideo] = useState(null);
  const [editingTitleVideoId, setEditingTitleVideoId] = useState(null);
  const [editingSectionId, setEditingSectionId] = useState(null);
  const [isDescriptionModalOpen, setDescriptionModalOpen] = useState(false);
  const [editedDescription, setEditedDescription] = useState('');

  const { register, handleSubmit, reset } = useForm();

  useEffect(() => {
    dispatch(fetchVideos());
    if (!selectedVideo && videosData?.length > 0 && videosData[0].videos?.length > 0) {
      setSelectedVideo(videosData[0].videos[0]);
    }
  }, [dispatch, videosData, selectedVideo]);

  const handleAccordionClick = (sectionId) => {
    setAccordionState((prevState) => ({
      ...prevState,
      [sectionId]: !prevState[sectionId],
    }));
  };

  const markAsWatched = (videoId) => {
    setWatchedVideos((prevState) => ({
      ...prevState,
      [videoId]: true,
    }));
  };

  const onSectionSubmit = (data) => {
    dispatch(createNewSection(data.sectionName));
    setIsModalOpen(false);
    reset();
  };

  const onVideoSubmit = (data) => {
    data.sectionId = sectionToAddVideo;
    dispatch(insertVideo(data));
    setIsModalOpen(false);
    reset();
    setSectionToAddVideo(null);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setSectionToAddVideo(null);
  };

  const openDescriptionModal = (description) => {
    setEditedDescription(description);
    setDescriptionModalOpen(true);
  };

  const closeDescriptionModal = () => {
    setDescriptionModalOpen(false);
  };

  const handleSectionSequenceChange = (e, sectionId) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      const newSequence = parseInt(e.target.textContent);
      setAccordionState((prevState) => ({
        ...prevState,
        [sectionId]: newSequence,
      }));
      dispatch(updateSectionSequence(sectionId, newSequence));
    }
  };

  const isSectionSequenceEditable = (sectionId) => {
    return true; // Habilita la edición de secuencia para todas las secciones
  };

  const handleVideoSequenceChange = (e, sectionId, videoId) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      const newSequence = parseInt(e.target.textContent);
      dispatch(updateVideoSequence(sectionId, videoId, newSequence));
    }
  };

  const isVideoSequenceEditable = (sectionId, videoId) => {
    return accordionState[sectionId];
  };

  const startEditingVideoTitle = (videoId) => {
    setEditingTitleVideoId(videoId);
  };

  const stopEditingVideoTitle = () => {
    setEditingTitleVideoId(null);
  };

  const startEditingSectionName = (sectionId) => {
    setEditingSectionId(sectionId);
  };

  const stopEditingSectionName = () => {
    setEditingSectionId(null);
  };

  const handleVideoTitleChange = async (videoId, newTitle) => {
    try {
      await dispatch(updateVideoTitle(videoId, newTitle));
      // Update the selected video's title as well
      setSelectedVideo((prevSelectedVideo) => ({
        ...prevSelectedVideo,
        title: newTitle,
      }));
      stopEditingVideoTitle();
    } catch (error) {
      console.error('Error al actualizar el título del video:', error);
    }
  };

  const handleSectionNameChange = async (sectionId, newSectionName) => {
    try {
      await dispatch(updateSectionName(sectionId, newSectionName));
      stopEditingSectionName();
    } catch (error) {
      console.error('Error al actualizar el nombre de la sección:', error);
    }
  };

  const handleDeleteSection = (sectionId) => {
    const confirmDelete = window.confirm('¿Seguro que quieres desactivar esta sección y todos sus videos?');
    if (confirmDelete) {
      dispatch(deactivateSection(sectionId));
    }
  };

  const handleDeleteVideo = (videoId) => {
    const confirmDelete = window.confirm('¿Seguro que quieres desactivar este video?');
    if (confirmDelete) {
      dispatch(deactivateVideo(videoId));
    }
  };

  const handleEditVideoUrl = (videoId) => {
    const newUrl = prompt('Ingresa la nueva URL del video:');
    if (newUrl !== null) {
      dispatch(updateVideoUrl(videoId, newUrl));
    }
  };

  const handleSaveDescription = async () => {
    try {
      await dispatch(updateVideoDescription(selectedVideo.id, editedDescription));
      closeDescriptionModal();
    } catch (error) {
      console.error('Error al actualizar la descripción del video:', error);
    }
  };

  return (
    <div className="videos-container">
      <div className="main-content">
        {selectedVideo ? (
          <>
            <div className="video-header">
              <h3>
                {editingTitleVideoId === selectedVideo.id ? (
                  <input
                    type="text"
                    defaultValue={selectedVideo.title}
                    onKeyDown={(e) => {
                      if (e.key === 'Enter') {
                        e.preventDefault();
                        handleVideoTitleChange(selectedVideo.id, e.target.value);
                      }
                    }}
                    onBlur={(e) => handleVideoTitleChange(selectedVideo.id, e.target.value)}
                  />
                ) : (
                  <>
                    {selectedVideo.title}
                    <span
                      onClick={() => startEditingVideoTitle(selectedVideo.id)}
                      className="edit-icon"
                    >
                      <AiOutlineEdit />
                    </span>
                  </>
                )}
              </h3>
            </div>
            <ReactPlayer
              url={selectedVideo.url}
              controls
              width="145vh"
              height="65vh"
              onEnded={() => markAsWatched(selectedVideo.id)}
            />
            <div className="video-content">
              <p>
                {selectedVideo.content}
                <span
                  onClick={() => openDescriptionModal(selectedVideo.content)}
                  className="edit-description-icon"
                >
                  <RiPencilLine />
                </span>
              </p>
            </div>
          </>
        ) : (
          <p>No hay un video seleccionado</p>
        )}
      </div>
      <div className="SideBarVideos">
        {videosData?.length > 0 ? (
          videosData.map((section) => (
            <div key={section.section_id}>
              <button onClick={() => handleAccordionClick(section.section_id)}>
                <div
                  className="sequence-box"
                  contentEditable={isSectionSequenceEditable(section.section_id)}
                  onKeyDown={(e) => handleSectionSequenceChange(e, section.section_id)}
                >
                  {section.section_sequence_number}
                </div>
                {editingSectionId === section.section_id ? (
                  <input
                    type="text"
                    defaultValue={section.section_name}
                    onKeyDown={(e) => {
                      if (e.key === 'Enter') {
                        e.preventDefault();
                        handleSectionNameChange(section.section_id, e.target.value);
                      }
                    }}
                    onBlur={(e) => handleSectionNameChange(section.section_id, e.target.value)}
                  />
                ) : (
                  <>
                    {section.section_name}
                    <span
                      onClick={() => startEditingSectionName(section.section_id)}
                      className="edit-icon"
                    >
                      <AiOutlineEdit />
                    </span>
                  </>
                )}
                <span
                  onClick={() => handleDeleteSection(section.section_id)}
                  className="delete-icon"
                >
                  <AiOutlineDelete />
                </span>
              </button>
              {accordionState[section.section_id] && (
                <>
                  {section.videos && section.videos.length > 0 ? (
                    section.videos.map((video) => (
                      <div
                        key={video.id}
                        onClick={() => setSelectedVideo(video)}
                        className="video-item"
                      >
                        <div
                          className="sequence-box"
                          contentEditable={isVideoSequenceEditable(section.section_id, video.id)}
                          onKeyDown={(e) =>
                            handleVideoSequenceChange(e, section.section_id, video.id)
                          }
                        >
                          {video.sequence_number}
                        </div>
                        <div
                          className="video-thumbnail"
                          style={{ backgroundImage: `url(${video.url})` }}
                        >
                          <span
                            onClick={() => handleEditVideoUrl(video.id)}
                            className="edit-icon" // Agregar ícono de lápiz para editar URL
                          >
                            <RiPencilLine />
                          </span>
                        </div>
                        <span>
                          {editingTitleVideoId === video.id ? (
                            <input
                              type="text"
                              defaultValue={video.title}
                              onKeyDown={(e) => {
                                if (e.key === 'Enter') {
                                  e.preventDefault();
                                  handleVideoTitleChange(video.id, e.target.value);
                                }
                              }}
                              onBlur={(e) => handleVideoTitleChange(video.id, e.target.value)}
                            />
                          ) : (
                            <>
                              {video.title}
                              <span
                                onClick={() => startEditingVideoTitle(video.id)}
                                className="edit-icon"
                              >
                                <AiOutlineEdit />
                              </span>
                            </>
                          )}
                          <span
                            onClick={() => handleDeleteVideo(video.id)}
                            className="delete-icon"
                          >
                            <AiOutlineDelete />
                          </span>
                        </span>
                        {watchedVideos[video.id] && <span>Visto</span>}
                      </div>
                    ))
                  ) : (
                    <p>Aún no hay videos disponibles en esta sección.</p>
                  )}
                  <button
                    className="add-video-button"
                    onClick={() => {
                      setIsModalOpen(true);
                      setSectionToAddVideo(section.section_id);
                    }}
                  >
                    Añadir Video a {section.section_name}
                  </button>
                </>
              )}
            </div>
          ))
        ) : (
          <p>No se ha agregado ninguna sección aún</p>
        )}
        <button
          className="add-section-button"
          onClick={() => {
            setIsModalOpen(true);
            setSectionToAddVideo(null);
          }}
        >
          <AiOutlinePlus />
        </button>
      </div>

      {isModalOpen && sectionToAddVideo && (
        <div className="modal">
          <div className="modal-content">
            <form onSubmit={handleSubmit(onVideoSubmit)}>
              <input
                type="text"
                placeholder="Título del video"
                {...register('title', { required: true })}
              />
              <input
                type="text"
                placeholder="URL del video"
                {...register('url', { required: true })}
              />
              <input
                type="text"
                placeholder="Contenido del video"
                {...register('content', { required: true })}
              />
              <input
                type="number"
                placeholder="Número de secuencia"
                {...register('sequenceNumber', { required: true })}
                onKeyDown={(e) => {
                  if (e.key === 'Enter') {
                    e.preventDefault();
                    handleSubmit(onVideoSubmit)();
                  }
                }}
              />
              <button type="submit">Agregar Video</button>
              <button onClick={closeModal}>Cerrar</button>
            </form>
          </div>
        </div>
      )}

      {isModalOpen && !sectionToAddVideo && (
        <div className="modal">
          <div className="modal-content">
            <form onSubmit={handleSubmit(onSectionSubmit)}>
              <input
                type="text"
                placeholder="Nombre de nueva sección"
                {...register('sectionName', { required: true })}
              />
              <button type="submit">Agregar sección</button>
              <button onClick={closeModal}>Cerrar</button>
            </form>
          </div>
        </div>
      )}

      {isDescriptionModalOpen && (
        <div className="modal">
          <div className="modal-content">
            <h2>Editar Descripción</h2>
            <textarea
              value={editedDescription}
              onChange={(e) => setEditedDescription(e.target.value)}
              rows="5"
              placeholder="Escribe la nueva descripción aquí"
            />
            <div className="modal-buttons">
              <button onClick={closeDescriptionModal}>Cancelar</button>
              <button onClick={handleSaveDescription}>Guardar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Videos;
