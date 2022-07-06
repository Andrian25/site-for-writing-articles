package by.teachmeskills.final_project.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("Post not found!");
    }

    public PostNotFoundException(Long id) {
        super("Post with id " + id + " not found!");
    }
}
