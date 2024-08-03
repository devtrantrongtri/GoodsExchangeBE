<<<<<<< HEAD
package com.uth.BE.Repository;

import com.uth.BE.Pojo.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
=======
package com.uth.BE.Repository;

import com.uth.BE.Pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
>>>>>>> 3173a20d1aa9afe7070a4d5f723f92f18665de1c
