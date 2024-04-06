package com.sweng.controller;

import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import com.sweng.entity.StoryBuilder;
import com.sweng.entity.StoryObject;
import com.sweng.utilities.GameService;
import com.sweng.utilities.ScenarioService;
import com.sweng.utilities.StoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StoryController {
    @Autowired
    private HttpSession httpSession;

    @Autowired
    private StoryService storyService;

    @Autowired
    private ScenarioService scenarioService;

    @Autowired
    private GameService gameService;


    @GetMapping("/create-story")
    public String createStory(Model model){
        String username = (String) httpSession.getAttribute("username");
        if(username == null){
            return "login";
        }
        return "create-story";
    }

    @PostMapping("/create-story/process")
    public String processCreateStory(@RequestParam("title") String title, @RequestParam("plot") String plot,
                                     @RequestParam("category") String category, Model model) {

        String username = (String) httpSession.getAttribute("username");
        if(username == null){
            return "login";
        }

        Story story = new StoryBuilder().setTitle(title).setPlot(plot).setInitialScenario(0).setCreator((String) httpSession.getAttribute("username")).setCategory(category).build();
        storyService.createStory(story);

        model.addAttribute("message", "Complimenti! Storia creata con successo. Ora crea un oggetto che potrà essere utilizzato all'interno della storia.");
        httpSession.setAttribute("currentStoryObjects", new ArrayList<StoryObject>());
        httpSession.setAttribute("currentStoryId", storyService.getMaxStoryId());
        return "create-object";

    }

    @GetMapping("/catalog")
    public String catalog(Model model) {
        String username = (String) httpSession.getAttribute("username");
        if(username == null){
            return "login";
        }
        model.addAttribute("stories", storyService.getStories());
        model.addAttribute("savedStories", storyService.getSavedIdStories(username));

        httpSession.removeAttribute("currentStoryId");
        httpSession.removeAttribute("currentStoryObjects");
        httpSession.removeAttribute("currentRiddles");
        httpSession.removeAttribute("currentScenarioId");
        httpSession.removeAttribute("scenarios");
        return "catalog";
    }

    @GetMapping("/visitor-catalog")
    public String visitorCatalog(Model model){
        model.addAttribute("stories", storyService.getStories());
        return "visitor-catalog";
    }

    @GetMapping("/visitor-catalog/show-story")
    public String showStory(@RequestParam("storyId") Integer storyId, Model model){

        Story story = storyService.getStoryById(storyId);
        model.addAttribute("storyTitle", story.getTitle());
        model.addAttribute("storyPlot", story.getPlot());
        model.addAttribute("storyCategory", story.getCategory());
        model.addAttribute("scenariosNumber", storyService.getScenariosNumberByStoryId(storyId));
        return "story";
    }

    @GetMapping("play-story")
    public String playStory(@RequestParam("storyId") Integer storyId, Model model){
        String username = (String) httpSession.getAttribute("username");
        if(username == null){
            return "login";
        }

        ArrayList<Scenario> storyScenarios = scenarioService.getScenariosByStoryId(storyId);
        model.addAttribute("storyScenarios", storyScenarios);

        for(Scenario scenario : storyScenarios){
            scenario.setNextScenarios(scenarioService.getNextScenariosByScenarioId(scenario.getId()));
        }

        Scenario initialScenario = storyScenarios.get(0);

        model.addAttribute("storyId", storyId);
        return gameService.loadScenario(initialScenario, model);
    }

    @GetMapping("/search-stories")
    public String searchStories(@RequestParam(required = false) String title, @RequestParam(required = false) String category,
                                @RequestParam(required = false) String creator, @RequestParam String action, Model model) {

        if(action.equals("play")){
            List<Story> stories = storyService.findStoriesByFilter(title, category, creator);
            model.addAttribute("stories", stories);

            return "catalog";
        }
        else if(action.equals("visit")){
            List<Story> stories = storyService.findStoriesByFilter(title, category, creator);
            model.addAttribute("stories", stories);
            return "visitor-catalog";
        }
        else{
            List<Story> stories = storyService.findStoriesByFilter(title, category,(String) httpSession.getAttribute("username"));
            model.addAttribute("stories", stories);
            return "owner-catalog";
        }

    }

    @GetMapping("/owner-catalog")
    public String ownerCatalog(Model model){
        String username = (String) httpSession.getAttribute("username");
        if(username == null){
            return "login";
        }
        model.addAttribute("stories", storyService.getStoriesByCreator((String) httpSession.getAttribute("username")));

        return "owner-catalog";
    }

    @GetMapping("/edit-scenario")
    public String editScenario(@RequestParam("storyId") Integer storyId, Model model) {
        String username = (String) httpSession.getAttribute("username");
        if(username == null){
            return "login";
        }

        List<Scenario> scenarios = scenarioService.getScenariosByStoryId(storyId);
        model.addAttribute("scenarios", scenarios);

        return "edit-scenario";
    }


}
