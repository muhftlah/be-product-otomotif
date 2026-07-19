package com.mfathullah.be_product_otomotif.service;

import com.mfathullah.be_product_otomotif.dto.request.StockAdjustRequest;
import com.mfathullah.be_product_otomotif.dto.request.StockReserveRequest;
import com.mfathullah.be_product_otomotif.dto.response.StockResponse;
import com.mfathullah.be_product_otomotif.exception.InsufficientStockException;
import com.mfathullah.be_product_otomotif.exception.ResourceNotFoundException;
import com.mfathullah.be_product_otomotif.model.Stock;
import com.mfathullah.be_product_otomotif.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(readOnly = true)
    public StockResponse findByVariantId(Integer variantId) {
        return toResponse(getStockOrThrow(variantId));
    }

    public StockResponse add(Integer variantId, StockAdjustRequest req) {
        Stock stock = getStockOrThrow(variantId);
        int newQty = stock.getQuantityOnHand() + req.quantity();
        if (newQty < 0) {
            throw new InsufficientStockException(
                    "Penyesuaian stok membuat quantity_on_hand negatif untuk variant " + variantId);
        }
        stock.setQuantityOnHand(newQty);
        return toResponse(stock);
    }

    public StockResponse adjust(Integer variantId, StockAdjustRequest req) {
        Stock stock = getStockOrThrow(variantId);
        int newQty = req.quantity();
        if (newQty < 0) {
            throw new InsufficientStockException(
                    "Penyesuaian stok membuat quantity_on_hand negatif untuk variant " + variantId);
        }
        stock.setQuantityOnHand(newQty);
        return toResponse(stock);
    }

    public StockResponse reserve(Integer variantId, StockReserveRequest req) {
        Stock stock = getStockOrThrow(variantId);
        int available = stock.getQuantityOnHand() - stock.getQuantityReserved();

        if (available < req.quantity()) {
            throw new InsufficientStockException(
                    "Stok tidak mencukupi. Tersedia: " + available + ", kurang: " + (req.quantity() - available));
        }
        stock.setQuantityReserved(stock.getQuantityReserved() + req.quantity());
        return toResponse(stock);
    }

    public StockResponse release(Integer variantId, StockReserveRequest req) {
        Stock stock = getStockOrThrow(variantId);
        int newReserved = stock.getQuantityReserved() - req.quantity();
        stock.setQuantityReserved(Math.max(newReserved, 0));
        return toResponse(stock);
    }

    public StockResponse reduction(Integer variantId, StockReserveRequest req) {
        Stock stock = getStockOrThrow(variantId);
        if (stock.getQuantityReserved() < req.quantity() || stock.getQuantityOnHand() < req.quantity()) {
            throw new InsufficientStockException("Reservasi tidak valid untuk variant " + variantId);
        }
        stock.setQuantityReserved(stock.getQuantityReserved() - req.quantity());
        stock.setQuantityOnHand(stock.getQuantityOnHand() - req.quantity());
        return toResponse(stock);
    }

    private Stock getStockOrThrow(Integer variantId) {
        return stockRepository.findByVariantId(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock untuk variant " + variantId + " tidak ditemukan"));
    }

    private StockResponse toResponse(Stock s) {
        return new StockResponse(
                s.getVariant().getId(), s.getVariant().getSku(),
                s.getQuantityOnHand(), s.getQuantityReserved(), s.getQuantityAvailable()
        );
    }
}