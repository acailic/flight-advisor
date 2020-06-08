package rs.interview.backend.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rs.interview.backend.domain.Comment;
import rs.interview.backend.service.dto.CommentDTO;


@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Mapping(source = "author.id", target = "author")
    CommentDTO toDto(Comment comment);



    @Mapping(source = "author", target = "author.id")
    Comment toEntity(CommentDTO cityDTO);


}
