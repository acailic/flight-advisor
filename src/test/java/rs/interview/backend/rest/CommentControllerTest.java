package rs.interview.backend.rest;


import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rs.interview.backend.config.JwtConfig;
import rs.interview.backend.domain.City;
import rs.interview.backend.domain.Comment;
import rs.interview.backend.repository.CommentRepository;
import rs.interview.backend.repository.UserRepository;
import rs.interview.backend.security.DomainUserDetailsService;
import rs.interview.backend.security.jwt.TokenProvider;
import rs.interview.backend.service.dto.CommentDTO;
import rs.interview.backend.service.mapper.CommentMapper;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests  REST controller.
 */

@EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class})
@AutoConfigureMockMvc
@WithMockUser
@WebMvcTest
public class CommentControllerTest {

    private static final String text_commment = "text comment";

    @Autowired
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentMockMvc;
    @Autowired
    private DomainUserDetailsService domainUserDetailsService;

    @Autowired
    private TokenProvider tokenProvider;
    @Mock
    private JwtConfig jwtConfig;

    private Comment comment;
    private City city;


    /**
     * Create an entity for this test.
     */
    public static Comment createEntity(EntityManager em) {
        Comment comment = new Comment();
        comment.setText(text_commment);
        return comment;
    }

    public static City createEntity(EntityManager em, Comment comment) {
        City city = new City();
        city.setCountry("serbia");
        city.setName("sabac");
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        city.setComments(commentList);
        return city;
    }



    //@Test
    @Transactional
    public void createComment() throws Exception {
        int databaseSizeBeforeCreate = commentRepository.findAll().size();

        // Create the comment
        CommentDTO commentDTO = commentMapper.toDto(comment);
        restCommentMockMvc.perform(post("/api/cities/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(commentDTO)))
                .andExpect(status().isCreated());

        // Validate the comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeCreate + 1);
        Comment testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getText()).isEqualTo(text_commment);
    }

    //@Test
    @Transactional
    public void createCommentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commentRepository.findAll().size();

        // Create the comment with an existing ID
        comment.setId(1L);
        CommentDTO commentDTO = commentMapper.toDto(comment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentMockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(commentDTO)))
                .andExpect(status().isBadRequest());

        // Validate the comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeCreate);
    }


    //@Test
    @Transactional
    public void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentRepository.findAll().size();
        // set the field null
        comment.setText(null);

        // Create the comment, which fails.
        CommentDTO commentDTO = commentMapper.toDto(comment);

        restCommentMockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(commentDTO)))
                .andExpect(status().isBadRequest());

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeTest);
    }

    //@Test
    @Transactional
    public void getAllComments() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the commentList
        restCommentMockMvc.perform(get("/api/cities/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(comment.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(text_commment)));
    }

    //@Test
    @Transactional
    public void getComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get the comment
        restCommentMockMvc.perform(get("/api/cities/1/comments/{id}", comment.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(comment.getId().intValue()))
                .andExpect(jsonPath("$.text").value(text_commment));
    }

    //@Test
    @Transactional
    public void getNonExistingComment() throws Exception {
        // Get the comment
        restCommentMockMvc.perform(get("/api/cities/1/comments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    //@Test
    @Transactional
    public void updateComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Update the comment
        Comment updatedComment = commentRepository.findById(comment.getId()).get();
        // Disconnect from session so that the updates on updatedcomment are not directly saved in db
        em.detach(updatedComment);
        updatedComment.setText(text_commment);
        CommentDTO commentDTO = commentMapper.toDto(updatedComment);

        restCommentMockMvc.perform(put("/api/cities/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(commentDTO)))
                .andExpect(status().isOk());

        // Validate the comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
        Comment testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getText()).isEqualTo(text_commment);
    }

    //@Test
    @Transactional
    public void updateNonExistingcomment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Create the comment
        CommentDTO commentDTO = commentMapper.toDto(comment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentMockMvc.perform(put("/api/cities/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(commentDTO)))
                .andExpect(status().isBadRequest());

        // Validate the comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    //@Test
    @Transactional
    public void deleteComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        int databaseSizeBeforeDelete = commentRepository.findAll().size();

        // Delete the comment
        restCommentMockMvc.perform(delete("/api/cities/1/comments/{id}", comment.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
