package be.zlz.kara.bin.services;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.dto.*;
import be.zlz.kara.bin.util.ReplyBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Deprecated
public interface BinService {
    Bin getByName(String name);

    Bin save(Bin bin);

    List<BinListDto> listBins(int page, int limit);

    BinDto getPagedBinDto(Bin bin, String requestUrl, int page, int limit);

    void deleteBin(Bin bin);

    String getSize(Bin bin);

    String updateSettings(String name, SettingViewModel settings);

    String updateSettings(String name, BinSettingsDto settings);

    void clearBin(String uuid);

    SettingViewModel getSettings(String name);

    BinSettingsDto getApiBinSettings(String name);

    String buildRequestUrl(HttpServletRequest request, String uuid);
}
