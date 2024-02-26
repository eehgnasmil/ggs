package ggs.ggs.goods;

import ggs.ggs.domain.GoodsFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsFileRepository extends JpaRepository<GoodsFile, Integer> {
}
