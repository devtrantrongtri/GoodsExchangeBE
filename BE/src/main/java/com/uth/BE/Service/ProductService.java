package com.uth.BE.Service;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.ProductImg;
import com.uth.BE.Entity.User;
import com.uth.BE.Repository.ProductImgRepository;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Service.Interface.IProductService;
import com.uth.BE.dto.req.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductImgRepository productImgRepository,UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productImgRepository = productImgRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error occurred while fetching all products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Product> getProductById(int id) {
        try {
            return productRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error occurred while fetching product by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void createProduct(Product product) {
        try {
            productRepository.save(product);
        } catch (Exception e) {
            System.err.println("Error occurred while creating product: " + e.getMessage());
        }
    }

    @Override
    public void deleteProductById(int id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error occurred while deleting product: " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) {
        try {
            productRepository.save(product);
        } catch (Exception e) {
            System.err.println("Error occurred while updating product: " + e.getMessage());
        }
    }

    public void changeStatusProduct(Product product, String newStatus){
        try{
            product.setStatus(newStatus);
            updateProduct(product);
        } catch (Exception e) {
            System.err.println("Error occurred while changing status product: " + e.getMessage());
        }
    }

    @Override
    public List<Product> searchProductsByUser(Optional<User> user) {
        try {
            return productRepository.findBySeller(user);
        } catch (Exception e) {
            System.err.println("Error occurred while searching products by user: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> searchProductsByCategory(Category category) {
        try {
            return productRepository.findByCategory(category); // Phương thức này cần được định nghĩa trong ProductRepository
        } catch (Exception e) {
            System.err.println("Error occurred while searching products by category: " + e.getMessage());
            return null;
        }
    }

    public List<Product> searchProductsByTitle(String title) {
        try {
            return productRepository.findByTitleContaining(title);
        } catch (Exception e) {
            System.err.println("Error occurred while searching products by title: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Product> searchProductsByPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        } catch (Exception e) {
            System.err.println("Error occurred while searching products by price: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Page<Product> getAllProductsWithPaginationAndSort(int pageNumber, int pageSize, String direction, String properties) {
        Sort.Direction directed = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, directed, properties);
        return productRepository.findAll(pageable);
    }

    @Override
    public List<ProductDTO> getProductsWithImage(String keyword) {
        List<ProductDTO> products = productRepository.findProductsByKeyword(keyword);
        for (ProductDTO product : products) {
            List<String> imageUrls = productImgRepository.findImageUrlsByProductId(product.getProductId());
            product.setImageUrls(imageUrls);
        }
        return products;
    }

    @Override
    public List<ProductDTO> getAllProductsWithImage() {
        List<ProductDTO> products = productRepository.findAllProducts();
        for (ProductDTO product : products) {
            List<String> imageUrls = productImgRepository.findImageUrlsByProductId(product.getProductId());
            product.setImageUrls(imageUrls);
        }
        return products;
    }

    @Override
    public Page<ProductDTO> getProductsByKeywordWithPaginationAndSort(String keyword, int pageNumber, int pageSize, String direction, String properties) {
        Sort.Direction directed = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, directed, properties);

        Page<Product> productsPage = productRepository.findByKeyword(keyword, pageable);

        return productsPage.map(product -> {
            List<String> imageUrls = product.getProductImgs().stream()
                    .map(ProductImg::getImgUrl)
                    .collect(Collectors.toList());

            return new ProductDTO(
                    product.getProduct_id(),
                    product.getCategory(),
                    product.getTitle(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStatus(),
                    imageUrls,
                    product.getCreated_at()
            );
        });
    }

    @Override
    public Page<ProductDTO> getAllProductsWithImagesWithSortAndPaging(int pageNumber, int pageSize, String direction, String properties) {
        Sort.Direction directed = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, directed, properties);

        // Lấy Page<Product> từ repository
        Page<Product> productsPage = productRepository.findAll(pageable);

        // Chuyển đổi từ Product sang ProductDTO và thêm ảnh
        return productsPage.map(product -> {
            List<String> imageUrls = productImgRepository.findImageUrlsByProductId(product.getProduct_id());

            return new ProductDTO(
                    product.getProduct_id(),
                    product.getCategory(),
                    product.getTitle(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStatus(),
                    imageUrls,
                    product.getCreated_at()
            );
        });
    }

    @Override
    public ProductDTO getProductDetail(int productId) {
        try {
            // Lấy thông tin sản phẩm từ productRepository
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();

                // Lấy danh sách hình ảnh từ productImgRepository
                List<String> imageUrls = productImgRepository.findImageUrlsByProductId(product.getProduct_id());

                // Tạo đối tượng ProductDTO và gán dữ liệu
                ProductDTO productDTO = new ProductDTO(
                        product.getProduct_id(),
                        product.getCategory(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStatus(),
                        imageUrls,
                        product.getCreated_at()
                );

                return productDTO;
            } else {
                return null;  // Nếu không tìm thấy sản phẩm, trả về null
            }
        } catch (Exception e) {
            System.err.println("Error occurred while fetching product detail: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<ProductDTO> getAllProductsByUsername(String username) {
        // Tìm kiếm user theo username
        Optional<User> user = userRepository.findByUsername(username);

        // Kiểm tra nếu user có tồn tại
        if (user.isPresent()) {
            // Tìm kiếm sản phẩm của user đó
            List<Product> products = productRepository.findBySeller(user.get());

            // Khởi tạo danh sách ProductDTO
            List<ProductDTO> productDTOList = new ArrayList<>();

            // Duyệt qua từng sản phẩm và chuyển đổi thành ProductDTO
            for (Product product : products) {
                // Lấy danh sách image URLs từ ProductImgRepository
                List<String> imageUrls = productImgRepository.findImageUrlsByProductId(product.getProduct_id());

                // Tạo ProductDTO mới và set các thuộc tính
                ProductDTO productDTO = new ProductDTO();
                productDTO.setTitle(product.getTitle());
                productDTO.setDescription(product.getDescription());
                productDTO.setPrice(product.getPrice());
                productDTO.setStatus(product.getStatus());
                productDTO.setImageUrls(imageUrls);
                productDTO.setCreate_at(product.getCreated_at());
                productDTO.setProductId(product.getProduct_id());

                // Thêm ProductDTO vào danh sách
                productDTOList.add(productDTO);
            }

            // Trả về danh sách ProductDTO
            return productDTOList;
        } else {
            // Trả về danh sách rỗng nếu user không tồn tại
            return new ArrayList<>();
        }
    }



}
