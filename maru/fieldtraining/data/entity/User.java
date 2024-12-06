package com.fieldtraining.data.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
@Builder
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@Column(nullable = false, unique = true)
	private String userId;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String role;

	@Column(nullable = false)
	private boolean isApproval;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student studentDetail;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Teacher teacherDetail;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Professor professorDetail;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CollegeManager collegeManagerDetail;



    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    
    public void setRoles(List<String> roles) {
        this.roles.clear(); // 기존 역할을 지우고
        this.roles.addAll(roles); // 새로운 역할을 추가
    }

    public void addRole(String role) {
        this.roles.add(role); // 역할 추가
    }
    
    public void removeRole(String role) {
        this.roles.remove(role); // 역할 삭제
    }
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)  // 수정: 직렬화 시에도 읽을 수 있도록
	@Override
	public String getUsername() {
	    return this.userId;
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)  // 수정: 인증 정보 직렬화 시 필요한 필드일 경우
	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)  // 수정
	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)  // 수정
	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)  // 수정
	@Override
	public boolean isEnabled() {
	    return true;
	}
}
