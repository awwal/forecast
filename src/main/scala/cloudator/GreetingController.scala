//package cloudator
//
//import org.springframework.stereotype.Controller
//import org.springframework.ui.Model
//import org.springframework.web.bind.annotation.{RequestMapping, RequestParam}
//
//@Controller class GreetingController {
//  @RequestMapping(Array("/greeting")) def greeting(@RequestParam(value = "name", required = false, defaultValue = "World") name: String, model: Model): String = {
//    model.addAttribute("name", name)
//    "greeting"
//  }
//
//  @RequestMapping(Array("/ui")) def ui(name: String, model: Model): String = {
//    val headers = new util.ArrayList[String]
//    headers.add("Day 1")
//    headers.add("Day 2")
//    val bodies = new util.ArrayList[String]
//    headers.add("Body 1")
//    headers.add("body 2")
//    model.addAttribute("headers", headers)
//    model.addAttribute("bodies", bodies)
//    "ui"
//  }
//}