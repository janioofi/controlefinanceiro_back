package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.CategoryDto;
import br.janioofi.financialcontrol.domain.entity.Category;
import br.janioofi.financialcontrol.domain.exceptions.RecordNotFoundException;
import br.janioofi.financialcontrol.domain.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository repository;
    private static final String NO_CATEGORIES = "No categories were found with: ";

    public List<CategoryDto> findAll(){
        log.info("Listing categories.");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public CategoryDto findById(Long id){
        return this.toDto(repository.findById(id).orElseThrow(() -> new RecordNotFoundException(NO_CATEGORIES + id)));
    }

    public CategoryDto create(CategoryDto categoryDto){
        Category category = new Category();
        category.setDescription(categoryDto.description());
        log.info("Creating new category: " + categoryDto.description());
        return this.toDto(repository.save(category));
    }

    public CategoryDto update(CategoryDto categoryDto, Long id){
        Category category = repository.findById(id).map(data -> {
            data.setDescription(categoryDto.description());
            return repository.save(data);
        }).orElseThrow(() -> new RecordNotFoundException(NO_CATEGORIES + id));
        return this.toDto(category);
    }

    public void delete(Long id){
        Optional<Category> category = repository.findById(id);
        if(category.isEmpty()) throw new RecordNotFoundException(NO_CATEGORIES + id);
        repository.deleteById(id);
    }

    private void validateDelete(){
        // TODO
    }

    private CategoryDto toDto(Category category){
        return new CategoryDto(category.getIdCategory(), category.getDescription());
    }
}
