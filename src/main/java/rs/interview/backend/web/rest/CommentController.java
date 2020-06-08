package rs.interview.backend.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rs.interview.backend.domain.City;
import rs.interview.backend.domain.Comment;
import rs.interview.backend.domain.User;
import rs.interview.backend.security.AuthoritiesConstants;
import rs.interview.backend.security.SecurityUtils;
import rs.interview.backend.service.CityService;
import rs.interview.backend.service.CommentService;
import rs.interview.backend.service.UserService;
import rs.interview.backend.service.dto.CommentDTO;
import rs.interview.backend.service.mapper.CommentMapper;
import rs.interview.backend.web.rest.errors.EntityForbiddenException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/cities")
public class CommentController {


    private final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final CityService cityService;

    public CommentController(CommentService commentService, CommentMapper commentMapper, UserService userService,
                             CityService cityService) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.cityService = cityService;
    }

    @GetMapping("/{cityId}/comments")
    public ResponseEntity<Page<CommentDTO>> getComments(@PathVariable Long cityId, Pageable pageable) {
        City city = cityService.findById(cityId);
        Page<CommentDTO> comments =
                commentService.findCommentsByCity(city,pageable)
                        .map(commentMapper::toDto);
        return ResponseEntity.ok().body(comments);
    }

    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    @GetMapping("/{cityId}/comments/{id}")
    public ResponseEntity<CommentDTO> getComments(@PathVariable Long cityId, @PathVariable Long id) {
        City city = cityService.findById(cityId);
        Comment comment = commentService.findCommentByIdAndCity(id, city);
        if (comment == null) {
            throw new EntityNotFoundException();
        }
        CommentDTO commentDTO = commentMapper.toDto(comment);
        return ResponseEntity.ok().body(commentDTO);
    }


    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    @PostMapping("/{cityId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long cityId,
                                                    @Valid @RequestBody CommentDTO commentDTO) {
        commentDTO.setId(null);
        User loggedUser = getLoggedUser();
        City city = cityService.findById(cityId);
        Comment commentCreate = commentMapper.toEntity(commentDTO);
        commentCreate.setAuthor(loggedUser);
        commentCreate.setCity(city);
        commentService.save(commentCreate);
        return ResponseEntity.ok().body(commentMapper.toDto(commentCreate));
    }

    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    @PutMapping("/{cityId}/comments/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @PathVariable Long cityId,
                                                    @Valid @RequestBody CommentDTO commentDTO) {
        User loggedUser = getLoggedUser();
        City city = cityService.findById(cityId);
        Comment comment = commentService.findCommentByIdAndCity(id, city);
        if (comment == null) {
            throw new EntityNotFoundException();
        }
        if (!loggedUser.equals(comment.getAuthor())) {
            throw new EntityForbiddenException("Forbidden comment update");
        }
        Comment commentUpdate = commentMapper.toEntity(commentDTO);
        commentUpdate.setAuthor(loggedUser);
        commentUpdate.setCity(city);
        commentService.save(commentUpdate);
        return ResponseEntity.ok().body(commentMapper.toDto(comment));
    }

    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    @DeleteMapping("/{cityId}/comments/{id}")
    public ResponseEntity deleteComment(@PathVariable Long cityId, @PathVariable Long id) {
        User loggedUser = getLoggedUser();
        City city = cityService.findById(cityId);
        Comment comment = commentService.findCommentByIdAndCity(id, city);
        if (comment == null) {
            throw new EntityNotFoundException();
        }
        if (!loggedUser.equals(comment.getAuthor())) {
            throw new EntityForbiddenException("Forbidden comment delete");
        }
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    private User getLoggedUser() {
        String userName = SecurityUtils.getCurrentUserLogin().orElseThrow(()
                -> new RuntimeException("Current user login not found"));
        return userService.findOneByUsername(userName);
    }
}
