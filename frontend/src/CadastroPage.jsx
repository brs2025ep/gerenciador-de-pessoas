import { useState, useEffect } from 'react';
import { RiResetLeftFill } from 'react-icons/ri';
import { FaSearch } from 'react-icons/fa';

const initialPessoaState = {
  id: null,
  nome: '',
  nascimento: '',
  cpf: '',
  email: '',
  cep: '',
  rua: '',
  numero: '',
  estado: '',
  cidade: '',
};

const CadastroPage = (
  { pessoaToEdit, onSubmit }
) => {
  const [formData, setFormData] = useState(initialPessoaState);
  const [errors, setErrors] = useState({});
  const [isCepSearchIconVisible, setIsCepSearchIconVisible] = useState(false);
  const [isAddressFillByCepActive, setIsAddressFillByCepActive] = useState(true);

  useEffect(() => {
    if (pessoaToEdit) {
      setFormData({
        id: pessoaToEdit.id || null,
        nome: pessoaToEdit.nome || '',
        nascimento: pessoaToEdit.nascimento || '',
        cpf: pessoaToEdit.cpf || '',
        email: pessoaToEdit.email || '',
        cep: pessoaToEdit.cep || '',
        rua: pessoaToEdit.rua || '',
        numero: pessoaToEdit.numero || '',
        cidade: pessoaToEdit.cidade || '',
        estado: pessoaToEdit.estado || '',
      });
    } else {
      setFormData(initialPessoaState);
    }
  }, [pessoaToEdit]);

  const displayCepSearchIcon = () => {
    setIsCepSearchIconVisible(true);
  };

  const hideCepSearchIcon = () => {
    setIsCepSearchIconVisible(false);
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.nome.trim()) newErrors.nome = 'Nome é obrigatório';

    if (formData.nascimento.length >= 10) {
      nascimentoDateValidation(newErrors);
    }

    if (formData.cpf.trim()) {
      if (formData.cpf.length < 10) {
        newErrors.cpf = 'CPF inválido';
      }
    }

    if (formData.email.trim()) {
      if (!/\S+@\S+\.\S+/.test(formData.email)) {
        newErrors.email = 'Email é inválido';
      }
    }

    if (formData.cep.trim()) {
      if (formData.cep.length < 8) {
        newErrors.cep = 'CEP precista ter 8 dígitos.'
      }
    }

    if (!isAddressFillByCepActive) { // Validação: Se o CEP não for encontrado, CIDADE, ESTADO, RUA E NÚMERO PASSAM A SER CAMPOS OBRIGATÓRIOS.
      if (!formData.cidade.trim()) newErrors.cidade = 'Cidade é obrigatório';
      if (!formData.estado.trim()) newErrors.cidade = 'Estado é obrigatório';
      if (!formData.rua.trim()) newErrors.cidade = 'Rua é obrigatória';
      if (!formData.numero.trim()) newErrors.cidade = 'Número é obrigatório';
    }

    return newErrors;
  };

  const nascimentoDateValidation = (newErrors) => {
    const [day, month, year] = formData.nascimento.split('/').map(Number);

    const dataNascimento = new Date(year, month - 1, day);
    const dataHoje = new Date();

    dataNascimento.setHours(0, 0, 0, 0);
    dataHoje.setHours(0, 0, 0, 0);

    const isValidDate =
      dataNascimento.getFullYear() === year &&
      dataNascimento.getMonth() === month - 1 &&
      dataNascimento.getDate() === day;

    if (!isValidDate) {
      newErrors.nascimento = 'Data de nascimento inválida';
    } else if (dataNascimento > dataHoje) {
      newErrors.nascimento = 'Data de nascimento não pode ser no futuro';
    }
  }

  const cepCodeValidation = async () => {
    console.log("Validando cep: " + formData.cep);
    try {
      const response = await apiService.checkCep(cepValue);

      if (response.status === 200) {
        if (response.data.erro === "true") {
          console.error("CEP não encontrado! Verifique o CEP.");

          // Limpar campos de endereço se o CEP não for encontrado
          setFormData(prevFormData => ({
            ...prevFormData,
            rua: '',
            cidade: '',
            estado: '',
          }));

        } else {
          // Definir autopreenchimento com CEP ativo para bloquear edição de formData.rua, formData.cidade e formData.estado
          setIsAddressFillByCepActive(true);

          // Preenche os campos do formulário com os dados da resposta
          setFormData(prevFormData => ({
            ...prevFormData,
            rua: response.data.logradouro || '', // "logradouro" é referente a Rua/Avenida/Praça e etc...
            cidade: response.data.localidade || '',
            estado: response.data.estado || '',
          }));
        }
      } else {
        console.error(`Erro na requisição ViaCEP: Status ${response.status}`);
      }
    } catch (error) {
      // Definir autopreenchimento com CEP desativado
      setIsAddressFillByCepActive(false);

      // Trata erros de requisição (ex: 400 Bad Request, problemas de rede)
      if (error.response) {
        // Erro de resposta do servidor (ex: 400)
        console.error(`Erro na requisição ViaCEP: Status ${error.response.status} - ${error.response.statusText}`);
      } else if (error.request) {
        // A requisição foi feita, mas não houve resposta
        console.error("Erro na requisição ViaCEP: Nenhuma resposta recebida.");
      } else {
        // Algo aconteceu na configuração da requisição que disparou um erro
        console.error("Erro na requisição ViaCEP:", error.message);
      }
      // Opcional: Limpar campos de endereço em caso de erro
      setFormData(prevFormData => ({
        ...prevFormData,
        rua: '',
        cidade: '',
        estado: '',
      }));
    }
  }

  const resetCadastroForm = () => {
    setFormData(initialPessoaState);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      setErrors({});
      console.log('Trying to submit Form Data.');

      if (onSubmit) {
        onSubmit(formData);
      }

      resetCadastroForm();
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    let formattedValue = value;

    if (name === 'nome') {
      formattedValue = value
        .split(' ')
        .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
        .join(' ');
    }

    if (name === 'nascimento') {
      formattedValue = value.replace(/\D/g, '');

      if (formattedValue.length > 2) {
        formattedValue = formattedValue.substring(0, 2) + '/' + formattedValue.substring(2);
      }
      if (formattedValue.length > 5) {
        formattedValue = formattedValue.substring(0, 5) + '/' + formattedValue.substring(5);
      }

      if (formattedValue.length > 10) {
        formattedValue = formattedValue.substring(0, 10);
      }
    }

    if (name === 'cep') {
      formattedValue = value.replace(/\D/g, '');

      if (formattedValue.length > 8) {
        formattedValue = formattedValue.substring(0, 8);
      }

      if (formattedValue.length === 8) {
        formattedValue = formattedValue.replace(/(\d{5})(\d{3})/, '$1-$2');
        displayCepSearchIcon();
      } else {
        hideCepSearchIcon();
      }
    }

    if (name === 'cpf') {
      const digitsOnly = value.replace(/\D/g, '');
      const limitedDigits = digitsOnly.substring(0, 11);

      if (limitedDigits.length > 0) {
        formattedValue = limitedDigits.replace(
          /^(\d{3})(\d{3})(\d{3})(\d{2})$/,
          '$1.$2.$3-$4'
        );
      } else {
        formattedValue = '';
      }
    }

    setFormData(prevFormData => ({
      ...prevFormData,
      [name]: formattedValue
    }));
    if (errors[name]) {
      setErrors(prevErrors => {
        const newErrors = { ...prevErrors };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  return (
    <div>
      <div className="container area-form">
        <h1>{formData.id ? 'Editar Pessoa' : 'Cadastrar Pessoa'}</h1>
        <hr className="solid"></hr>
        <form onSubmit={handleSubmit}>
          <div className="form-row-group">
            <label className="block font-medium">Nome</label>
            <input
              type="text"
              name="nome"
              value={formData.nome}
              placeholder="Digite o nome. Exemplo: Gonçalves Dias.."
              onChange={handleChange}
              required />
            {errors.nome && <p className="text-red-500 text-sm">{errors.nome}</p>}
          </div>

          <div className="form-row-group">
            <label className="block font-medium">Data nascimento</label>
            <input
              type="text"
              name="nascimento"
              value={formData.nascimento}
              placeholder="Digite a data de nascimento no formato dia/mês/ano. Exemplo: 25/12/1900.."
              onChange={handleChange}
            />
            {errors.nascimento && <p className="text-red-500 text-sm">{errors.nascimento}</p>}
          </div>

          <div className="form-row-group">
            <label className="block font-medium">CPF</label>
            <input
              type="text"
              name="cpf"
              value={formData.cpf}
              placeholder="Digite os números do CPF da pessoa..."
              onChange={handleChange}
            />
            {errors.cpf && <p className="text-red-500 text-sm">{errors.cpf}</p>}
          </div>
          <div className="form-row-group">
            <label className="block font-medium">E-mail</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              placeholder="Digite o Email da pessoa..."
              onChange={handleChange}
            />
            {errors.email && <p className="text-red-500 text-sm">{errors.email}</p>}
          </div>
          <fieldset>
            <legend>Endereço</legend>
            <div className="form-row-group">
              <label htmlFor="cep">Cep</label>
              <div className="relative">
                <input
                  type="text"
                  name="cep"
                  value={formData.cep}
                  placeholder="Digite o CEP da Pessoa.."
                  onChange={handleChange} />
                {isCepSearchIconVisible && (
                  <div
                    onClick={() => cepCodeValidation()}
                  >
                    <FaSearch /> Buscando
                  </div>
                )}
              </div>
              {errors.cep && <p className="text-red-500 text-sm">{errors.cep}</p>}
            </div>
            <div className="grouped-fields">
              <div className="form-row-group">
                <label htmlFor="rua">Rua</label>
                <input
                  type="text"
                  name="rua"
                  value={formData.rua}
                  placeholder="Digita a Rua da Pessoa.."
                  onChange={handleChange}
                  readOnly={isAddressFillByCepActive} />
                {errors.rua && <p className="text-red-500 text-sm">{errors.rua}</p>}
              </div>
              <div className="form-row-group">

                <label htmlFor="numero">Número</label>
                <input
                  type="number"
                  name="numero"
                  value={formData.numero}
                  placeholder="Digite o número da Residência da Pessoa.."
                  onChange={handleChange} />
                {errors.numero && <p className="text-red-500 text-sm">{errors.numero}</p>}
              </div>
            </div>
            <div className="grouped-fields">
              <div className="form-row-group">
                <label htmlFor="cidade">Cidade</label>
                <input
                  type="text"
                  name="cidade"
                  value={formData.cidade}
                  placeholder="Digite o nome da Cidade da Pessoa..."
                  onChange={handleChange}
                  readOnly={isAddressFillByCepActive} />
                {errors.cidade && <p className="text-red-500 text-sm">{errors.cidade}</p>}
              </div>
              <div className="form-row-group">
                <label htmlFor="estado">Estado</label>
                <input
                  type="text"
                  name="estado"
                  value={formData.estado}
                  placeholder="Digite o nome do Estado da Pessoa..."
                  onChange={handleChange}
                  readOnly={isAddressFillByCepActive}
                />
                {errors.estado && <p className="text-red-500 text-sm">{errors.estado}</p>}
              </div>
            </div>
          </fieldset>
          <button
            type="submit"
          >
            {formData.id ? 'Salvar Mudanças' : 'Salvar'}
          </button>
          {formData.id && (
            <>
              Cancelar<RiResetLeftFill
                className="action-icon"
                onClick={() => resetCadastroForm()}
                title="Cancelar a edição"
              />
            </>
          )}

        </form>
      </div>
    </div>

  );
};

export default CadastroPage;