package by.teachmeskills.final_project.controller;

import by.teachmeskills.final_project.exception.PostNotFoundException;
import by.teachmeskills.final_project.model.Post;
import by.teachmeskills.final_project.repository.PostRepository;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class BlogController {
  private static final String REDIRECT_BLOG = "redirect:/blog";

  private final PostRepository postRepository;

  @GetMapping("/blog")
  public String blogMain(Model model) {
    Iterable<Post> posts = postRepository.findAll();
    model.addAttribute("posts", posts);
    return "blog-main";
  }

  @GetMapping("/blog/add")
  public String blogAdd() {
    return "blog-add";
  }

  @PostMapping("/blog/add")
  public String blogPostAdd(
      @RequestParam String title, @RequestParam String anons, @RequestParam String fullText) {
    Post post = new Post(title, anons, fullText);
    postRepository.save(post);
    return REDIRECT_BLOG;
  }

  @GetMapping("/blog/{id}")
  public ModelAndView blogDetails(@PathVariable(value = "id") long id) {
    if (!postRepository.existsById(id)) {
      return new ModelAndView(REDIRECT_BLOG);
    }
    ModelAndView mv = new ModelAndView("blog-details");
    Optional<Post> post = postRepository.findById(id);
    ArrayList<Post> res = new ArrayList<>();
    post.ifPresent(res::add);
    mv.addObject("post", res);
    return mv;
  }

  @GetMapping("/blog/{id}/edit")
  public String blogEdit(@PathVariable(value = "id") long id, Model model) {
    if (!postRepository.existsById(id)) {
      return REDIRECT_BLOG;
    }

    Optional<Post> post = postRepository.findById(id);
    ArrayList<Post> res = new ArrayList<>();
    post.ifPresent(res::add);
    model.addAttribute("post", res);
    return "blog-edit";
  }

  @PostMapping("/blog/{id}/edit")
  public String blogPostUpdate(
      @PathVariable(value = "id") long id,
      @RequestParam String title,
      @RequestParam String anons,
      @RequestParam String fullText) {
    Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    post.setTitle(title);
    post.setAnons(anons);
    post.setFullText(fullText);
    postRepository.save(post);

    return REDIRECT_BLOG;
  }

  @PostMapping("/blog/{id}/remove")
  public String blogPostDelete(@PathVariable(value = "id") long id) {
    Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    postRepository.delete(post);
    return REDIRECT_BLOG;
  }
}
