package org.tuprimernegocio.tuprimernegocio.videos;

import com.tuprimernegocio.library.database.jooq.videos.tables.pojos.Sections;
import com.tuprimernegocio.library.database.jooq.videos.tables.pojos.Videos;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/createSection")
    public ResponseEntity<Map<String, Object>> createSection(@RequestHeader Map<String, String> headers,
            @RequestBody Sections section) {
        Map<String, Object> result = videoService.createSection(headers, section.getName(), section.getAddedBy());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getAllVideosGroupedBySections")
    public ResponseEntity<List<Map<String, Object>>> getAllVideosGroupedBySections(
            @RequestHeader Map<String, String> headers) {
        List<Map<String, Object>> result = videoService.getAllVideosGroupedBySections(headers);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/insertVideo")
    public ResponseEntity<Map<String, Object>> insertVideo(
            @RequestHeader Map<String, String> headers,
            @RequestBody Videos videos) {
        Map<String, Object> result = videoService.insertVideo(headers,
                videos.getTitle(),
                videos.getUrl(),
                videos.getContent(),
                videos.getSequenceNumber(),
                videos.getSectionId(),
                videos.getAddedBy());

        // Envuelve el resultado en un ResponseEntity y devuelve
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/updateSectionSequence")
    public ResponseEntity<String> updateSectionSequence(
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> requestPayload) {
        Integer sectionId = (Integer) requestPayload.get("section_id");
        Integer newSequenceNumber = (Integer) requestPayload.get("new_sequence_number");
        Integer addedBy = (Integer) requestPayload.get("addedBy"); // Agregamos el parámetro addedBy
        videoService.updateSectionSequence(headers, sectionId, newSequenceNumber, addedBy);
        return new ResponseEntity<>("Section sequence updated successfully", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/updateVideoSequence")
    public ResponseEntity<String> updateVideoSequence(
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> requestPayload) {
        Integer sectionId = (Integer) requestPayload.get("section_id");
        Integer videoId = (Integer) requestPayload.get("video_id");
        Integer newSequenceNumber = (Integer) requestPayload.get("new_sequence_number");
        Integer addedBy = (Integer) requestPayload.get("addedBy"); // Agregamos el parámetro addedBy
        videoService.updateVideoSequence(headers, sectionId, videoId, newSequenceNumber, addedBy);
        return new ResponseEntity<>("Video sequence updated successfully", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/updateSectionName")
    public ResponseEntity<String> updateSectionName(
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> requestPayload) {
        Integer sectionId = (Integer) requestPayload.get("section_id");
        String newName = (String) requestPayload.get("new_name");
        videoService.updateSectionName(headers, sectionId, newName);
        return new ResponseEntity<>("Section name updated successfully", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/updateVideoTitle")
    public ResponseEntity<String> updateVideoTitle(
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> requestPayload) {
        Integer videoId = (Integer) requestPayload.get("video_id");
        String newTitle = (String) requestPayload.get("new_title");
        videoService.updateVideoTitle(headers, videoId, newTitle);
        return new ResponseEntity<>("Video title updated successfully", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/deactivateVideo")
    public ResponseEntity<String> deactivateVideo(
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> requestPayload) {
        Integer videoId = (Integer) requestPayload.get("video_id");
        videoService.deactivateVideo(headers, videoId);
        return new ResponseEntity<>("Video deactivated successfully", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/deactivateSection")
    public ResponseEntity<String> deactivateSection(
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> requestPayload) {
        Integer sectionId = (Integer) requestPayload.get("section_id");
        videoService.deactivateSection(headers, sectionId);
        return new ResponseEntity<>("Section deactivated successfully", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/updateVideoUrl/{videoId}")
    public ResponseEntity<String> updateVideoUrl(
            @RequestHeader Map<String, String> headers,
            @PathVariable int videoId,
            @RequestBody Map<String, String> requestPayload) {
        String newUrl = requestPayload.get("newUrl");
        videoService.updateVideoUrl(headers, videoId, newUrl);
        return new ResponseEntity<>("Video URL updated successfully", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/updateVideoDescription/{videoId}")
    public ResponseEntity<String> updateVideoDescription(
            @RequestHeader Map<String, String> headers,
            @PathVariable int videoId,
            @RequestBody Map<String, String> requestPayload) {
        String newDescription = requestPayload.get("newDescription");
        videoService.updateVideoDescription(headers, videoId, newDescription);
        return new ResponseEntity<>("Video description updated successfully", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/markVideoAsWatched/{userId}/{videoId}")
    public ResponseEntity<String> markVideoAsWatched(
            @RequestHeader Map<String, String> headers,
            @PathVariable int userId,
            @PathVariable int videoId) {
        videoService.markVideoAsWatched(headers, userId, videoId);

        return new ResponseEntity<>("Video marked as watched successfully", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/unmarkVideoAsWatched/{userId}/{videoId}")
    public ResponseEntity<String> unmarkVideoAsWatched(
            @RequestHeader Map<String, String> headers,
            @PathVariable int userId,
            @PathVariable int videoId) {
        videoService.unmarkVideoAsWatched(headers, userId, videoId);

        return new ResponseEntity<>("Video unmarked as watched successfully", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getAllVideosBySectionsAndUser/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getAllVideosBySectionsAndUser(
            @RequestHeader Map<String, String> headers,
            @PathVariable Integer userId) {
        List<Map<String, Object>> result = videoService.getAllVideosBySectionsAndUser(headers, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
