package br.com.cotiinformatica.repositories.interfaces;

import java.util.List;

public interface IBaseRepository<TEntity, TKey> {

	void create(TEntity obj) throws Exception;

	void update(TEntity obj) throws Exception;

	void delete(TKey id) throws Exception;

	List<TEntity> findAll() throws Exception;

	TEntity findById(TKey id) throws Exception;
}
