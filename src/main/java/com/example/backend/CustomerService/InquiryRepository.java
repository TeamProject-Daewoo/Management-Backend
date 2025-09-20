package com.example.backend.CustomerService;

    import java.util.List;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import java.util.Optional;


    public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
        List<Inquiry> findByUser_Username(String username);

        Page<Inquiry> findAllByStatus(InquiryStatus status, Pageable pageable);

        @Query("SELECT i FROM Inquiry i LEFT JOIN FETCH i.inquiryFiles WHERE i.id = :id")
    Optional<Inquiry> findByIdWithFiles(@Param("id") Long id);

    }

