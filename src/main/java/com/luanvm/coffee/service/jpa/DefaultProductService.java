
package com.luanvm.coffee.service.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.luanvm.coffee.domain.Product;
import com.luanvm.coffee.repository.ProductRepository;
import com.luanvm.coffee.service.ProductService;

@Service("productService")
@Repository
@Transactional
public class DefaultProductService implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Transactional(readOnly=true)
	public List<Product> findAll() {
		return Lists.newArrayList(productRepository.findAll());
	}

	@Transactional(readOnly=true)
	public Product findById(Integer id) {
		return productRepository.findOne(id);
	}

	public Product save(Product TH_Product) {
		return productRepository.save(TH_Product);
	}

	@Transactional(readOnly=true)
	public Page<Product> findAllByPage(Pageable pageable) {
		return productRepository.findAll(pageable);
	}
}
