package com.artiselite.warehouse.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.artiselite.warehouse.models.Role;
import com.artiselite.warehouse.models.UserEntity;
import com.artiselite.warehouse.models.UserEntityDto;
import com.artiselite.warehouse.services.RolesRepository;
import com.artiselite.warehouse.services.UserRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RolesRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping({"", "/"})
	public String showUserEntityList (Model model) {
		List<UserEntity> userEntity = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("userEntity", userEntity);
		return "user/index";
	}
	
	@GetMapping({"/create"})
	public String showCreateUserEntity (Model model) {
		UserEntityDto userEntityDto = new UserEntityDto();
		List<Role> listRoles = roleRepository.findAll();
		model.addAttribute("userEntityDto", userEntityDto);
		model.addAttribute("listRoles", listRoles);
		return "user/create-user";
	}
	
	@PostMapping({"/create"})
	public String createUserEntity (
			Model model,
		@Valid @ModelAttribute UserEntityDto userEntityDto,
		BindingResult result
		) {
		if (result.hasErrors()) {
			List<Role> listRoles = roleRepository.findAll();
			model.addAttribute("listRoles", listRoles);
			return "user/create-user";
		}
		
		//save record
		UserEntity userEntity = new UserEntity();
		userEntity.setName(userEntityDto.getName());
		userEntity.setUsername(userEntityDto.getUsername());
		userEntity.setPassword(passwordEncoder.encode(userEntityDto.getPassword()));
//		userEntity.setRoles(null)
		
		userRepository.save(userEntity);
		
		return "redirect:/users";
	}
	
	@GetMapping({"/edit"})
	public String showEditUserEntity (Model model, @RequestParam int id) {
		
		try {
			UserEntity userEntity = userRepository.findById(id).get();
			model.addAttribute("userEntity", userEntity);
			
			UserEntityDto userEntityDto = new UserEntityDto();
			userEntityDto.setName(userEntity.getName());
			userEntityDto.setUsername(userEntity.getUsername());
			userEntityDto.setPassword(userEntity.getPassword());
			
			model.addAttribute("userEntityDto", userEntityDto);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			return "redirect:/users";
		}
		return "user/edit-user";
	}
//	
//	@PostMapping({"/edit"})
//	public String updateUserEntity (Model model, @RequestParam int id,
//			@Valid @ModelAttribute UserEntityDto UserEntityDto,
//			BindingResult result
//			) {
//		
//		try {
//			UserEntity UserEntity = userRepository.findById(id).get();
//			model.addAttribute("UserEntity", UserEntity);
//			
//			if (result.hasErrors()) {
//				return "UserEntitys/edit-UserEntity";
//			}
//			
//			if (!UserEntityDto.getImageFile().isEmpty()) {
//				//delete old image
//				String uploadDir = "public/images/";
//				Path oldImagePath = Paths.get(uploadDir + UserEntity.getImageFileName());
//				
//				try {
//					Files.delete(oldImagePath);
//				} catch (Exception e) {
//					System.out.println("Exception: " + e.getMessage());
//				}
//				
//				//save new file
//				MultipartFile image = UserEntityDto.getImageFile();
//				Date createdAt = new Date();
//				
//				String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
//				
//				try (InputStream inputStream = image.getInputStream()) {
//					Files.copy(inputStream, Paths.get(uploadDir + storageFileName), 
//							StandardCopyOption.REPLACE_EXISTING);
//				}
//				UserEntity.setImageFileName(storageFileName);
//				
//			}
//			
//			UserEntity.setName(UserEntityDto.getName());
//			UserEntity.setBrand(UserEntityDto.getBrand());
//			UserEntity.setCategory(UserEntityDto.getCategory());
//			UserEntity.setPrice(UserEntityDto.getPrice());
//			UserEntity.setDescription(UserEntityDto.getDescription());
//			
//			userRepository.save(UserEntity);
//			
//			model.addAttribute("UserEntityDto", UserEntityDto);
//		} catch (Exception e) {
//			System.out.println("Exception: " + e.getMessage());
//			return "redirect:/UserEntitys";
//		}
//		return "UserEntitys/edit-UserEntity";
//	}
//	
//	@PreAuthorize("hasRole('MANAGER')")
//	@GetMapping({"/delete"})
//	public String deleteUserEntity (@RequestParam int id) {
//		
//		try {
//			UserEntity UserEntity = userRepository.findById(id).get();
//			
//			//delete UserEntity image
//			Path imagePath = Paths.get("public/images/" + UserEntity.getImageFileName());
//			
//			try {
//				Files.delete(imagePath);
//			} catch (Exception e) {
//				System.out.println("Exception: " + e.getMessage());
//			}
//			userRepository.delete(UserEntity);
//
//		} catch (Exception e) {
//			System.out.println("Exception: " + e.getMessage());
//		}
//				
//		return "redirect:/UserEntitys";
//	}
}
