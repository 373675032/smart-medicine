package world.xuewei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.FirstAidCategory;
import world.xuewei.entity.FirstAidGuide;
import world.xuewei.entity.User;
import world.xuewei.service.FirstAidCategoryService;
import world.xuewei.service.FirstAidService;
import world.xuewei.utils.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FirstAidController {
    @Autowired
    private FirstAidCategoryService firstAidCategoryService;
    @Resource
    private FirstAidService firstAidService;
    /**
     * 急救指南页面
     */
    @GetMapping("/first-aid")
    public String firstAid(Model model) {
        // 获取所有分类
        List<FirstAidCategory> categories = firstAidService.listCategories();
        model.addAttribute("categories", categories);
        
        // 获取所有分类下的指南内容
        Map<Integer, List<FirstAidGuide>> allGuides = new HashMap<>();
        for (FirstAidCategory category : categories) {
            List<FirstAidGuide> guides = firstAidService.listGuidesByCategory(category.getId());
            allGuides.put(category.getId(), guides);
        }
        
        model.addAttribute("allGuides", allGuides);
        return "first-aid";
    }

    /**
     * 获取分类下的指南列表
     */
    @GetMapping("/first-aid/category/{categoryId}")
    @ResponseBody
    public List<FirstAidGuide> getGuidesByCategory(@PathVariable Integer categoryId) {
        return firstAidService.listGuidesByCategory(categoryId);
    }

    /**
     * 指南详情页
     */
    @GetMapping("/first-aid-detail")
    public String firstAidDetail(@RequestParam Integer id, Model model, HttpSession session) {
   
        // 获取指南详情
        FirstAidGuide guide = firstAidService.getGuideDetail(id);
        model.addAttribute("guide", guide);
        return "first-aid-detail";
    }

    /**
     * 管理页面
     */
    @GetMapping("/all-first-aid")
    public String allFirstAid(Model model, 
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer categoryId) {
        
        // 分页获取分类列表
        Map<String, Object> categoryPage = firstAidService.pageCategories(page, pageSize);
        model.addAttribute("categories", categoryPage.get("list"));
        model.addAttribute("categoryTotal", categoryPage.get("total"));
        model.addAttribute("categoryPages", categoryPage.get("pages"));
        model.addAttribute("categoryCurrent", page);
        
        // 分页获取指南列表
        Map<String, Object> guidePage = firstAidService.pageGuides(page, pageSize, categoryId);
        model.addAttribute("list", guidePage.get("list"));
        model.addAttribute("guideTotal", guidePage.get("total"));
        model.addAttribute("guidePages", guidePage.get("pages"));
        model.addAttribute("guideCurrent", page);
        model.addAttribute("categoryId", categoryId);
        
        return "all-first-aid";
    }

    /**
     * 添加/编辑页面
     */
    @GetMapping("/add-first-aid")
    public String addFirstAid(Integer id, Model model) {
        if (Assert.notEmpty(id)) {
            FirstAidGuide guide = firstAidService.getGuideDetail(id);
            model.addAttribute("guide", guide);
        }
        // 获取所有分类供选择
        List<FirstAidCategory> categories = firstAidService.listCategories();
        model.addAttribute("categories", categories);
        return "add-first-aid";
    }

    /**
     * 保存指南
     */
    @PostMapping("/first-aid/save")
    @ResponseBody
    public Map<String, Object> saveGuide(@RequestBody FirstAidGuide guide) {
        Map<String, Object> result = new HashMap<>();
        try {
            firstAidService.saveGuide(guide);
            result.put("code", "SUCCESS");
        } catch (Exception e) {
            result.put("code", "ERROR");
            result.put("msg", "保存失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 保存分类
     */
    @PostMapping("/first-aid/category/save")
    @ResponseBody
    public Map<String, Object> saveCategory(@RequestBody FirstAidCategory category) {
        Map<String, Object> result = new HashMap<>();
        try {
            firstAidService.saveCategory(category);
            result.put("code", "SUCCESS");
        } catch (Exception e) {
            result.put("code", "ERROR");
            result.put("msg", "保存失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除指南
     */
    @PostMapping("/first-aid/delete/{id}")
    @ResponseBody
    public Map<String, Object> deleteGuide(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            firstAidService.deleteGuide(id);
            result.put("code", "SUCCESS");
        } catch (Exception e) {
            result.put("code", "ERROR");
            result.put("msg", "删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除分类
     */
    @PostMapping("/first-aid/category/delete/{id}")
    @ResponseBody
    public Map<String, Object> deleteCategory(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            firstAidService.deleteCategory(id);
            result.put("code", "SUCCESS");
        } catch (Exception e) {
            result.put("code", "ERROR");
            result.put("msg", "删除失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/first-aid/category/page")
    @ResponseBody
    public Map<String, Object> categoryPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Map<String, Object> result = new HashMap<>();
        Page<FirstAidCategory> page = new Page<>(pageNum, pageSize);
        IPage<FirstAidCategory> categoryPage = firstAidService.pageCategory(page);
        
        result.put("list", categoryPage.getRecords());
        result.put("total", categoryPage.getTotal());
        result.put("pages", categoryPage.getPages());
        result.put("current", categoryPage.getCurrent());
        
        return result;
    }

    @GetMapping("/first-aid/page")
    @ResponseBody
    public Map<String, Object> guidePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer categoryId) {

        return firstAidService.pageGuides(pageNum, pageSize, categoryId);
    }
} 