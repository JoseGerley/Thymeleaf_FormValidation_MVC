package co.edu.icesi.ci.thymeval.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;
	
	@NotBlank(groups = {ParteTwo.class, EditableAtributte.class})
	@Size(min=2,groups = {ParteTwo.class, EditableAtributte.class})
	private String name;
	
	@NotNull(groups = {ParteOne.class})
	@Size(min=8, groups = {ParteOne.class})
	private String password;
	
	@NotNull(groups = {ParteOne.class, EditableAtributte.class})
	@Size(min=3, groups = {ParteOne.class, EditableAtributte.class})
	private String username;
	
	@NotBlank(groups = {ParteTwo.class, EditableAtributte.class})
	@Email(groups = {ParteTwo.class, EditableAtributte.class})
	private String email;
	
	@NotNull(groups = {ParteTwo.class, EditableAtributte.class})
	private UserType type;
	
	@NotNull(groups = {ParteOne.class, EditableAtributte.class})
	@Past(groups = {ParteOne.class, EditableAtributte.class})
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	
	@NotNull(groups = {ParteTwo.class, EditableAtributte.class})
	private UserGender gender;
	
//	@OneToMany
//	private List<Appointment> appointments;
}
