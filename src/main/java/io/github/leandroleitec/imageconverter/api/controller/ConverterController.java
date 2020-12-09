package io.github.leandroleitec.imageconverter.api.controller;

import io.github.leandroleitec.imageconverter.api.dto.ImageDto;
import io.github.leandroleitec.imageconverter.service.ImageConversorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("conversor")
@RequiredArgsConstructor
public class ConverterController {

    private final ImageConversorService conversorService;

    @PostMapping("base64")
    public ImageDto convert(@RequestBody ImageDto image) throws IOException {
        String content = conversorService.convert(image.getContent(), image.getFileName());
        image.setContent(content);
        return image;
    }

    @PostMapping("file")
    public ImageDto convert(@RequestParam("file") MultipartFile file) throws IOException {
        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
        String fileName = file.getOriginalFilename().split("\\.")[0];

        String content = conversorService.convert(base64Image, fileName);

        return new ImageDto(fileName, content);
    }
}
