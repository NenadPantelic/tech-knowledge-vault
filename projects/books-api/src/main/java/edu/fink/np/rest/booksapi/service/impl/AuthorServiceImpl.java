package edu.fink.np.rest.booksapi.service.impl;

import edu.fink.np.rest.booksapi.dto.request.NewAuthor;
import edu.fink.np.rest.booksapi.dto.response.AuthorDTO;
import edu.fink.np.rest.booksapi.dto.response.MinimalAuthorDTO;
import edu.fink.np.rest.booksapi.exception.ApiException;
import edu.fink.np.rest.booksapi.exception.ErrorReport;
import edu.fink.np.rest.booksapi.mapper.AuthorMapper;
import edu.fink.np.rest.booksapi.model.Author;
import edu.fink.np.rest.booksapi.repository.AuthorRepository;
import edu.fink.np.rest.booksapi.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static edu.fink.np.rest.booksapi.common.Constants.ORDER_TO_SORT_DIRECTION_MAP;

@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {

    private static final String SOME_AUTHORS_DO_NOT_EXIST = "Some authors do not exist!";

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorDTO addAuthor(NewAuthor newAuthor) {
        log.info("Adding a new author: {}", newAuthor);

        Author author = Author.builder()
                .fullName(newAuthor.fullName())
                .biography(newAuthor.biography())
                .build();

        author = authorRepository.save(author);
        return AuthorMapper.mapToDTO(author);
    }

    @Override
    public Author findAuthor(Long id) {
        log.info("Looking for an author with id: {}.", id);
        return authorRepository.findById(id).orElseThrow(() -> new ApiException(
                ErrorReport.NOT_FOUND.withMessage("Author not found.")
        ));
    }

    @Override
    public AuthorDTO getAuthor(Long id) {
        return AuthorMapper.mapToDTO(findAuthor(id));
    }

    @Override
    public Set<Author> findAuthors(Set<Long> authorIds) {
        log.info("Find authors by their ids: {}", authorIds);
        if (authorIds == null || authorIds.isEmpty()) {
            return Set.of();
        }

        List<Author> authors = (List) authorRepository.findAllById(authorIds);

        if (authors.size() != authorIds.size()) {
            log.error(SOME_AUTHORS_DO_NOT_EXIST);
            throw new ApiException(ErrorReport.NOT_FOUND
                    .withMessage(SOME_AUTHORS_DO_NOT_EXIST)
            );
        }

        return new HashSet<>(authors);
    }

    @Override
    public List<MinimalAuthorDTO> listAuthors(int page, int size, String sortBy, String sortOrder) {
        log.info("List books using the following paging parameters: page = {}, size = {}, sort by = {}, order = {}",
                page, size, sortBy, sortOrder);

        // TODO: validate sortBy field
        Sort.Direction sortDirection = ORDER_TO_SORT_DIRECTION_MAP.getOrDefault(sortOrder, Sort.Direction.DESC);
        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(sortDirection, sortBy)
        );

        Page<Author> authors = authorRepository.findAll(pageRequest);
        return AuthorMapper.mapToDTOList(authors);
    }
}
