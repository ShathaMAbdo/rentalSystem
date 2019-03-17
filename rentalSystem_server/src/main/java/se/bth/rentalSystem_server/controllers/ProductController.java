package se.bth.rentalSystem_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.bth.rentalSystem_server.models.Product;
import se.bth.rentalSystem_server.repository.ProductRepository;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final Logger log = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductRepository productRepo;

    public ProductController(ProductRepository customerRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/products")
    Collection<Product> products() {
        return productRepo.findAll();
    }

    @GetMapping("/product/{id}")
    ResponseEntity<?> getProduct(@PathVariable Long id) {
        Optional<Product> product = productRepo.findById(id);
        return product.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws URISyntaxException {
        log.info("Request to create product: {}", product);
        Product result = productRepo.save(product);
        return ResponseEntity.created(new URI("/api/product/" + result.getId())).body(result);
    }

    @RequestMapping(value = "/product/{id}/image", method = RequestMethod.POST)
    public String handleFormUpload(@PathVariable String id,@RequestParam("file") MultipartFile file) throws IOException {
//        if (!file.isEmpty()) {
//            BufferedImage src = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
//            File destination = new File("C:/Users/shatha/Documents/image" + id + ".png"); // File directory with file name
//            ImageIO.write(src, "png", destination);
//            //Save the id you have used to create the file name in the DB. You can retrieve the image in future with the ID.
//        }
            try{
            Product product = productRepo.findById(Long.valueOf(id)).get();
                Byte[] byteObjects = new Byte[file.getBytes().length];
                int i = 0;
                for (byte b : file.getBytes()){
                    byteObjects[i++] = b;
                }
                product.setImage(byteObjects);
                productRepo.save(product);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                log.error("Error occurred", e);
            }
        return "redirect:/product/" + id + "/show";
    }
    @PutMapping("/product")
    ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product,boolean ok) {
        log.info("Request to update product: {}", product);
        product.resource_returned(ok);
        Product result = productRepo.save(product);
        return ResponseEntity.ok().body(result);
    }


    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        log.info("Request to delete product: {}", id);
        productRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
