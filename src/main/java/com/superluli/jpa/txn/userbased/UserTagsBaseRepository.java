package com.superluli.jpa.txn.userbased;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserTagsBaseRepository extends
		CrudRepository<UserTagsEntity, String> {

	public List<String> getTagsByUserId(String userId);
}
