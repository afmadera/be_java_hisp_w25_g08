package com.grupo08.socialmeli.service;

import com.grupo08.socialmeli.dto.response.FollowDto;
import com.grupo08.socialmeli.dto.response.FollowersDto;
import com.grupo08.socialmeli.dto.response.FollowedDTO;
import com.grupo08.socialmeli.entity.Buyer;
import com.grupo08.socialmeli.entity.Seller;
import com.grupo08.socialmeli.entity.User;
import com.grupo08.socialmeli.exception.BadRequestException;
import com.grupo08.socialmeli.exception.NotFoundException;
import com.grupo08.socialmeli.repository.IBuyerRepository;
import com.grupo08.socialmeli.repository.ISellerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    IBuyerRepository buyerRepository;
    ISellerRepository sellerRepository;

    public UserServiceImpl(IBuyerRepository buyerRepository, ISellerRepository sellerRepository) {
        this.buyerRepository = buyerRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public FollowDto follow(int idBuyer, int idSeller) {

        Optional<Buyer> buyer = buyerRepository.findById(idBuyer);

        Optional<Seller> seller = sellerRepository.findById(idSeller);

        if(buyer.isEmpty()) throw new NotFoundException("No se encuentra comprador con ese ID.");

        if(seller.isEmpty()) throw new NotFoundException("No hay vendedor con ese ID.");

        Optional<Seller> sellerToRemove = buyer.get().getFollowing().stream()
                .filter(s -> s.getId() == idSeller)
                .findFirst();

        if (sellerToRemove.isPresent()) {
            throw new BadRequestException("No puedes seguir un vendedor que ya sigues.");
        }
        
        buyer.get().addFollowingSeller(seller.get());

        return new FollowDto(idSeller, seller.get().getName());
    }

    public FollowersDto getFollowers(int idSeller) {
        Optional<Seller> seller = sellerRepository.findById(idSeller);

        if(seller.isEmpty()) throw new NotFoundException("No hay vendedor con ese ID.");

        List<User> followers = seller.get().getFollowers();

        FollowersDto followersDto = new FollowersDto(
                seller.get().getId(),
                seller.get().getName(),
                followers.stream().map(this::convertFollowerDto).collect(Collectors.toList())
        );
        return followersDto;
    }

    private FollowDto convertFollowerDto(User user){
        return new FollowDto(user.getId(), user.getName());
    }

    @Override
    public void unfollow(int idBuyer, int idSeller) {

        Optional<Buyer> buyer = buyerRepository.findById(idBuyer);

        Optional<Seller> seller = sellerRepository.findById(idSeller);

        if(buyer.isEmpty()) throw new NotFoundException("No se encuentra comprador con ese ID.");

        if(seller.isEmpty()) throw new NotFoundException("No hay vendedor con ese ID.");

        buyer.get().unFollowSeller(seller.get());

    }

    @Override
    public FollowedDTO getFollowedSellers(int userId, String order) {
        Optional<Buyer> user = this.buyerRepository.findById(userId);
        if(user.isEmpty()){
            throw new NotFoundException("El usuario con el id:"+userId+" no se encontró");
        }

        if(!(user.get() instanceof Buyer)){
            throw new BadRequestException("El usuario con el id:"+userId+" no es un comprador");
        }
        Buyer buyer = user.get();

        FollowedDTO buyerResponseDTO = new FollowedDTO();
        buyerResponseDTO.setUser_id(buyer.getId());
        buyerResponseDTO.setUser_name(buyer.getName());

        List<Seller> followedSellers = new ArrayList<>();

        if(order!=null){
            if(order.equalsIgnoreCase("name_asc")){
                followedSellers = buyer.getFollowing().stream()
                        .sorted(Comparator.comparing(User::getName)).toList();
            }else if(order.equalsIgnoreCase("name_desc")){
                followedSellers = buyer.getFollowing().stream()
                        .sorted(Comparator.comparing(User::getName).reversed()).toList();
            }else{
                throw new BadRequestException("El valor del parámetro order no es correcto");
            }
        }else{
            followedSellers = buyer.getFollowing();
        }


        List<FollowDto> followedSellersDTO = new ArrayList<>();

        for(Seller seller: followedSellers){
            followedSellersDTO.add(new FollowDto(seller.getId(),seller.getName()));
        }

        buyerResponseDTO.setFollowed(followedSellersDTO);

        return buyerResponseDTO;
    }
}
