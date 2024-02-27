package ggs.ggs.goods;

import ggs.ggs.domain.*;
import ggs.ggs.dto.FileDto;
import ggs.ggs.dto.GoodsDto;
import ggs.ggs.dto.GoodsOptionDto;
import ggs.ggs.member.MemberRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("goodsServiceImpl")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private final GoodsRepository goodsRepository;

    @Autowired
    private final GoodsOptionRepository optionRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    @Qualifier("goodsFileServiceImpl")
    private final GoodsFileServiceImpl fileService;


    @Override
    public List<GoodsDto> findAll(int category) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> goodsList= this.goodsRepository.findTop10ByCategoryWithLikeCount(category, pageable);;
        List<GoodsDto> goodsDtos = new ArrayList<>();

        for (Object[] objects : goodsList) {
            Goods goods = (Goods)objects[0];
            GoodsDto goodsDto = new GoodsDto(goods);
            Long likeCount = (Long) objects[1];

            List<String> colors = optionRepository.findDistinctColorsByGoodsIdx(goods);
            List<String> sizes = optionRepository.findDistinctSizesByGoodsIdx(goods);
            goodsDtos.add(goodsDto);
            goodsDto.setColors(colors);
            goodsDto.setSizes(sizes);
            goodsDto.setLikeCnt(likeCount.intValue());
        }
        return goodsDtos;
    }

    @Override
    public List<GoodsDto> findAll(Integer category, String name) {
        Member member = memberRepository.findByid(name).get();
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> goodsList = this.goodsRepository.findTop10ByCategoryWithLikeCount(category,member,pageable);
        List<GoodsDto> goodsDtos = new ArrayList<>();



        for (Object[] objects : goodsList) {
            Goods goods = (Goods)objects[0];
            GoodsDto goodsDto = new GoodsDto(goods);
            Long likeCount = (Long) objects[1];
            boolean likeTF = (boolean) objects[2];
            System.out.println(likeTF);

            List<String> colors = optionRepository.findDistinctColorsByGoodsIdx(goods);
            List<String> sizes = optionRepository.findDistinctSizesByGoodsIdx(goods);
            goodsDtos.add(goodsDto);
            goodsDto.setColors(colors);
            goodsDto.setSizes(sizes);
            goodsDto.setLikeCnt(likeCount.intValue());
            goodsDto.setLikeTF(likeTF);

        }
        return goodsDtos;
    }

    // 페이징
    @Override
    public Page<GoodsDto> findAll(int sortValue, int page, int category) {
        String sortField = null;
        switch (sortValue) {
            case 0:
                sortField = "idx";
                break;
            case 1:
                sortField = "likeCnt";
                break;
            case 2:
                sortField = "reviewCnt";
                break;
            case 3:
                sortField = "orderCnt";
                break;
            case 4:
                sortField = "highPrice";
                break;
            case 5:
                sortField = "lowPrice";
                break;
        }

        Pageable pageable = PageRequest.of(page, 12);
        Page<Object[]> goodsPage = goodsRepository.findByCategoryWithCounts(category,sortField,pageable);

        List<GoodsDto> goodsDtoList = goodsPage.getContent().stream()
                .map(objects -> {
                    Goods goods = (Goods)objects[0];
                    Long likeCount = (Long) objects[1];
                    Long orderCount = (Long) objects[2];

                    return new GoodsDto(goods, likeCount.intValue(), orderCount.intValue());
                })
                .collect(Collectors.toList());

        Page<GoodsDto> goodsDtoPage = new PageImpl<>(goodsDtoList, goodsPage.getPageable(), goodsPage.getTotalElements());

        return goodsDtoPage;
    }

    @Override
    public Page<GoodsDto> findAll(Integer sortValue, int page, Integer category, String name) {
        Member member = memberRepository.findByid(name).get();
        String sortField = null;
        switch (sortValue) {
            case 0:
                sortField = "idx";
                break;
            case 1:
                sortField = "likeCnt";
                break;
            case 2:
                sortField = "reviewCnt";
                break;
            case 3:
                sortField = "orderCnt";
                break;
            case 4:
                sortField = "highPrice";
                break;
            case 5:
                sortField = "lowPrice";
                break;
        }

        Pageable pageable = PageRequest.of(page, 12);
        Page<Object[]> goodsPage = goodsRepository.findByCategoryWithCounts(category,sortField,member,pageable);

        for (Object[] objects : goodsPage.getContent()) {
            System.out.println(objects[0]);
        }

        List<GoodsDto> goodsDtoList = goodsPage.getContent().stream()
                .map(objects -> {
                    Goods goods = (Goods)objects[0];
                    Long likeCount = (Long) objects[1];
                    Long orderCount = (Long) objects[2];
                    boolean likeTF = (boolean) objects[3];

                    return new GoodsDto(goods, likeCount.intValue(), orderCount.intValue(),likeTF);
                })
                .collect(Collectors.toList());

        Page<GoodsDto> goodsDtoPage = new PageImpl<>(goodsDtoList, goodsPage.getPageable(), goodsPage.getTotalElements());

        return goodsDtoPage;
    }


    @Override
    public GoodsDto getGoods(Integer idx) {
        Goods goods = this.goodsRepository.findById(idx)
                .orElseThrow(() -> new NoSuchElementException("No Goods with idx " + idx));
        List<String> colors = optionRepository.findDistinctColorsByGoodsIdx(goods);
        List<String> sizes = optionRepository.findDistinctSizesByGoodsIdx(goods);

        GoodsDto goodsDto = new GoodsDto(goods);
        goodsDto.setColors(colors);
        goodsDto.setSizes(sizes);
        goodsDto.setLikeTF(false);

        return goodsDto;
    }

    @Override
    public GoodsDto getGoods(Integer idx, String name) {
        Member member = memberRepository.findByid(name).get();

        Tuple object = this.goodsRepository.findByIdxWithBoolean(idx,member);
        Goods goods = (Goods) object.get(0);
        boolean likeTF = (boolean) object.get(1);
        System.out.println(object.toString());
        List<String> colors = optionRepository.findDistinctColorsByGoodsIdx(goods);
        List<String> sizes = optionRepository.findDistinctSizesByGoodsIdx(goods);

        GoodsDto goodsDto = new GoodsDto(goods);
        goodsDto.setColors(colors);
        goodsDto.setSizes(sizes);
        goodsDto.setLikeTF(likeTF);

        return goodsDto;
    }

    // 사이즈 가져오기
    @Override
    public List<String> selectSizes(Integer goodsIdx, String color) {
        Goods goods = this.goodsRepository.findById(goodsIdx)
                .orElseThrow(() -> new NoSuchElementException("No Goods with idx " + goodsIdx));
        List<String> sizes = optionRepository.findSizesByGoodsIdxandcolor(goods, color);
        return sizes;

    }

    @Override
    public Integer insert(GoodsDto goodsDto) throws IOException {
        Integer saveIdx = null;

        Goods goods = new Goods(goodsDto);


        List<GoodsFile> goodsFiles = new ArrayList<>();
        List<GoodsOption> goodsOptions = new ArrayList<>();


        for (FileDto fileDto : goodsDto.getFileDtos()) {
            if (!(fileDto.getFile().isEmpty())) {
                FileDto nfileDto = fileService.insert(fileDto.getFile());
                nfileDto.setGoods(goods);
                GoodsFile goodsFile = new GoodsFile(nfileDto);
                goodsFiles.add(goodsFile);
            }
        }

        for (GoodsOptionDto goodsOptionDto : goodsDto.getGoodsOptionDtos()) {
            goodsOptionDto.setGoods(goods);
            GoodsOption goodsOption = new GoodsOption(goodsOptionDto);
            goodsOptions.add(goodsOption);
        }

        goods.getFiles().addAll(goodsFiles); // Goods에 파일 목록 추가
        goods.getGoodsOptions().addAll(goodsOptions); // Goods에 파일 목록 추가
        saveIdx = goodsRepository.save(goods).getIdx();
        return saveIdx;

    }

    @Override
    public Integer update(GoodsDto goodsDto) throws IOException {
        List<GoodsFile> goodsFiles = new ArrayList<>();
        // Goods 엔터티 저장
        Goods goods = new Goods(goodsDto);

        List<GoodsOption> goodsOptions = new ArrayList<>();

        for (FileDto fileDto : goodsDto.getFileDtos()) {
            if ("D".equals(fileDto.getStatus())) { // 삭제
                fileService.delete(fileDto.getSfile());
            } else if ("N".equals(fileDto.getStatus())) {
                if (!fileDto.getFile().isEmpty()) {
                    FileDto nfileDto = fileService.insert(fileDto.getFile());
                    nfileDto.setGoods(goods);
                    GoodsFile goodsFile = new GoodsFile(nfileDto);
                    goodsFiles.add(goodsFile);
                }
            } else {
                fileDto.setGoods(goods);
                GoodsFile file = new GoodsFile(fileDto);
                goodsFiles.add(file);
            }
        }

        for (GoodsOptionDto goodsOptionDto : goodsDto.getGoodsOptionDtos()) {
            if (goodsOptionDto.getStatus() != null) {
                goodsOptionDto.setGoods(goods);
                GoodsOption goodsOption = new GoodsOption(goodsOptionDto);
                goodsOptions.add(goodsOption);
            }
        }

        goods.getFiles().addAll(goodsFiles); // Goods에 파일 목록 추가
        goods.getGoodsOptions().addAll(goodsOptions); // Goods에 파일 목록 추가
        Integer saveIdx = goodsRepository.save(goods).getIdx();

        return saveIdx;
    }

    @Override
    public void delete(Integer idx) {
        Goods goods = goodsRepository.findById(idx).orElse(null);

        if (goods != null) {
            // Goods 엔티티 삭제 전에 연결된 OrderItem의 goods 속성을 null로 설정
            for (OrderItem orderItem : goods.getOrderItems()) {
                orderItem.setGoodsToNull();
            }
        }
        for (GoodsFile file : goods.getFiles()) {
            fileService.delete(file.getSfile());
        }
        goodsRepository.delete(goods);
    }
}